package com.example.telepathy.domain.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.telepathy.data.entities.User
import com.example.telepathy.domain.repositories.UserRepository
import com.example.telepathy.domain.serialization.deserializeUser
import com.example.telepathy.domain.serialization.serializeUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

class BluetoothRepository(
    private val context: Context
) {

    private val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private val _discoveredUsers = MutableStateFlow<List<String>>(emptyList())
    val discoveredUsers: StateFlow<List<String>> = _discoveredUsers

    private var serverSocket: BluetoothServerSocket? = null
    private var isAdvertising = false
    private val appUuid: UUID = UUID.fromString("12345678-1234-5678-1234-567812345678")


    private fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    fun startAdvertising(localUser: User) {
        Log.d("Bluetooth", "isAdvertising: $isAdvertising")
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
                serverSocket = try {
                    bluetoothAdapter.listenUsingRfcommWithServiceRecord("TelepathyService", appUuid)
                } catch (e: IOException) {
                    Log.e("Bluetooth", "Failed to create server socket", e)
                    null
                }
                if (serverSocket != null) {
                    Log.d("Bluetooth", "Advertising started...")
                    while (isAdvertising) {
                        val socket = serverSocket?.accept()
                        socket?.let {
                            Log.d("Bluetooth", "Connection accepted from ${it.remoteDevice.name}")

                            sendUser(it, localUser)

                            startCommunication(it)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("Bluetooth", "Failed to start advertising", e)
            } finally {
                stopAdvertising()
            }
        }
    }

    fun stopAdvertising() {
        isAdvertising = false
        serverSocket?.close()
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
        val discoveredDevices = mutableSetOf<String>()
        val targetUuid = UUID.fromString("12345678-1234-5678-1234-567812345678")

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
                        discoveredDevices.add(device.address)
                        _discoveredUsers.value = discoveredDevices.toList()
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

    private fun startCommunication(socket: BluetoothSocket) {
        val inputStream: InputStream = socket.inputStream

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val buffer = ByteArray(1024)
                var bytesRead: Int
                while (true) {
                    bytesRead = inputStream.read(buffer)
                    val message = String(buffer, 0, bytesRead)
                    Log.d("Bluetooth", "Received message: $message")
                }
            } catch (e: Exception) {
                Log.e("Bluetooth", "Error during communication", e)
            } finally {
                socket.close()
            }
        }
    }
}
