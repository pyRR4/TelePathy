package com.example.telepathy.domain.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.example.telepathy.data.entities.User
import com.example.telepathy.domain.dtos.UserDTO
import com.example.telepathy.domain.mappers.UserMapper.toDTO
import com.example.telepathy.domain.mappers.UserMapper.toLong
import com.example.telepathy.domain.serialization.deserializeUser
import com.example.telepathy.domain.serialization.isCompleteJson
import com.example.telepathy.domain.serialization.serializeUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.util.UUID

class BluetoothRepository(
    private val context: Context
) {
    var discoverableThread: Thread? = null

    private val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private val _discoveredUsers = MutableStateFlow<Set<UserDTO>>(emptySet())
    val discoveredUsers: StateFlow<Set<UserDTO>> = _discoveredUsers

    private val _isDiscoverable = MutableStateFlow<Boolean>(false)
    val isDiscoverable: StateFlow<Boolean> = _isDiscoverable.asStateFlow()

    private var serverSocket: BluetoothServerSocket? = null
    private var isAdvertising = false
    private val appUuid: UUID = UUID.fromString("12345678-1234-5678-1234-567812345678")


    private fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    fun startAdvertising(localUser: User) {
        startAdvertising(localUser, appUuid) {
            sendUser(localUser)
            stopAdvertising()
        }
    }

    fun stopAdvertising() {
        isAdvertising = false
        serverSocket?.close()
        stopDiscoverableMode()
        Log.d("Bluetooth", "Advertising stopped.")
    }

    @SuppressLint("MissingPermission")
    fun startScan() {
        if (!hasPermission(Manifest.permission.BLUETOOTH_SCAN)) {
            Log.e("Bluetooth", "Missing permission: BLUETOOTH_SCAN")
            return
        }
        if (!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            Log.e("Bluetooth", "Missing permission: ACCESS_FINE_LOCATION")
            return
        }

        Log.d("Bluetooth", "Started scanning...")
        val targetUuid = UUID.fromString("12345678-1234-5678-1234-567812345678")

        // Dodajemy testowego użytkownika do listy----------------------------------------------
        CoroutineScope(Dispatchers.Main).launch {
            val testUser = User(
                id = 0,
                name = "Test User",
                description = "This is a test user.",
                color = Color.White.toLong(),
                avatar = null,
                deviceId = "TEST_DEVICE_1"
            )
            val updatedList = _discoveredUsers.value.toMutableSet().apply {
                add(testUser.toDTO())
            }
            _discoveredUsers.value = updatedList
            Log.d("Bluetooth", "Test user added: ${testUser.name}")
        }

        CoroutineScope(Dispatchers.Main).launch {
            val testUser2 = User(
                id = 0,
                name = "Duzy",
                description = "on jest duzy",
                color = Color.Black.toLong(),
                avatar = null,
                deviceId = "TEST_DEVICE_2"
            )
            val updatedList = _discoveredUsers.value.toMutableSet().apply {
                add(testUser2.toDTO())
            }
            _discoveredUsers.value = updatedList
            Log.d("Bluetooth", "Test user 2 added: ${testUser2.name}")
        }

        val discoveryReceiver = BluetoothDiscoveryReceiver(
            onDeviceFound = { device ->
                val name = device.name ?: "Unknown Device"
                val address = device.address
                Log.d("Bluetooth", "Discovered device: $name, $address, ${device.uuids}")

                device.fetchUuidsWithSdp()
            },
            onUuidFetched = { device, uuids ->
                Log.d("Bluetooth", "Fetched UUIDs for device ${device.name}: $uuids")

                if (uuids.contains(targetUuid)) {
                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            val socket = device.createRfcommSocketToServiceRecord(targetUuid)
                            socket.connect()
                            Log.d("Bluetooth", "Connected to device: $device")

                            socket.exchangeUsers(localUser)
                            stopScan()
                        } catch (e: IOException) {
                            Log.e("Bluetooth", "Failed to connect to device: ${device.name}", e)
                        }
                    }
                    Log.d("Bluetooth", "Device ${device.name} matches the app's UUID!")
                }
            }
        )

        context.registerReceiver(discoveryReceiver, BluetoothDiscoveryReceiver.Companion.filter)
        bluetoothAdapter.startDiscovery()
    }


    @SuppressLint("MissingPermission")
    fun stopScan() {
        bluetoothAdapter.cancelDiscovery()
        Log.d("Bluetooth", "Scanning stopped.")
    }

    fun handleReceivedUser(user: User) {
        CoroutineScope(Dispatchers.Main).launch {
            val newUser = User(
                id = 0,
                name = user.name,
                description = user.description,
                color = user.color,
                avatar = user.avatar,
                deviceId = user.deviceId
            )
            val updatedList = _discoveredUsers.value.toMutableSet().apply {
                add(newUser.toDTO())
            }
            _discoveredUsers.value = updatedList

            Log.d("Bluetooth", "Handling received user: ${newUser.name}")
        }
    }

    fun BluetoothSocket.sendUser(user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val outputStream = outputStream
                val userJson = serializeUser(user)
                outputStream.write("USER:$userJson\n".toByteArray())
                Log.d("Bluetooth", "User sent: $userJson")
            } catch (e: Exception) {
                Log.e("Bluetooth", "Failed to send user data", e)
            }
        }
    }

    fun BluetoothSocket.receiveUser() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val inputStream = inputStream
                val buffer = ByteArray(4096)
                val receivedData = StringBuilder()

                var bytesRead: Int
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    receivedData.append(String(buffer, 0, bytesRead))

                    if (receivedData.endsWith("}\n")) {
                        break
                    }
                }

                val message = receivedData.toString().trim()
                if (message.startsWith("USER:")) {
                    val userJson = message.removePrefix("USER:")
                    val user = deserializeUser(userJson)
                    Log.d("Bluetooth", "User received: $user")
                    handleReceivedUser(user)
                }
            } catch (e: Exception) {
                Log.e("Bluetooth", "Failed to receive user data", e)
            }
        }
    }

    fun BluetoothSocket.exchangeUsers(localUser: User) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val outputStream = outputStream
                val inputStream = inputStream

                // Send hello message to initiate exchange
                outputStream.write("HELLO\n".toByteArray())
                Log.d("Bluetooth", "HELLO message sent")

                val buffer = ByteArray(4096)
                val receivedData = StringBuilder()
                var bytesRead: Int
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    receivedData.append(String(buffer, 0, bytesRead))
                    if (receivedData.endsWith("\n")) break
                }

                val message = receivedData.toString().trim()
                when (message) {
                    "HELLO" -> {
                        // If HELLO received, send user data
                        sendUser(localUser)
                        receiveUser()
                    }
                    else -> Log.d("Bluetooth", "Unexpected message: $message")
                }
            } catch (e: IOException) {
                Log.e("Bluetooth", "Error during user exchange", e)
            } finally {
                close()
            }
        }
    }

    fun enableDiscoverable(localUser: User) {
        if (!bluetoothAdapter.isEnabled) {
            Log.e("Bluetooth", "Bluetooth is not enabled")
            return
        }

        discoverableThread = Thread {
            while (true) {
                try {
                    val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                        putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 30)
                    }
                    context.startActivity(discoverableIntent)
                    startScan(localUser)
                    Thread.sleep(29 * 1000L)
                } catch (e: InterruptedException) {
                    Log.e("Bluetooth", "Discoverable thread interrupted", e)
                    break
                    _isDiscoverable.value = false
                }
            }
        }
        discoverableThread?.start()
        _isDiscoverable.value = true
    }

    fun stopDiscoverableMode() {
        discoverableThread?.interrupt()
        _isDiscoverable.value = false
        stopScan()
        discoverableThread = null
    }

    @SuppressLint("MissingPermission")
    fun startAdvertising(
        localUser: User,
        appUuid: UUID,
        onConnectionAccepted: BluetoothSocket.() -> Unit
    ) {
        enableDiscoverable(localUser)

        if (isAdvertising) return

        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
            Log.e("Bluetooth", "Missing permission: BLUETOOTH_CONNECT")
            return
        }
        if (!hasPermission(Manifest.permission.BLUETOOTH_ADVERTISE)) {
            Log.e("Bluetooth", "Missing permission: BLUETOOTH_ADVERTISE")
            return
        }
        if (!bluetoothAdapter.isEnabled) {
            Log.e("Bluetooth", "Bluetooth is not enabled.")
            return
        }

        Log.d("Bluetooth", "Using UUID: $appUuid")

        isAdvertising = true

        CoroutineScope(Dispatchers.IO).launch {
            try {
                serverSocket =
                    bluetoothAdapter.listenUsingRfcommWithServiceRecord("TelepathyService", appUuid)
                Log.d("Bluetooth", "Advertising started...")

                while (isAdvertising) {
                    val socket = serverSocket?.accept()
                    socket?.let {
                        Log.d("Bluetooth", "Connection accepted from ${it.remoteDevice.name}")
                        it.exchangeUsers(User(id = 0, name = "Local User", description = "", color = Color.White.toLong(), avatar = null, deviceId = "LOCAL_DEVICE"))
                        it.onConnectionAccepted()
                    }
                }
            } catch (e: Exception) {
                Log.e("Bluetooth", "Failed to start advertising", e)
            } finally {
                stopAdvertising()
            }
        }
    }
}
