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
        if (isAdvertising) return
        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
            Log.e("Bluetooth", "Missing permission: BLUETOOTH_CONNECT")
            return
        }
        isAdvertising = true

        CoroutineScope(Dispatchers.IO).launch {
            try {
                serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("TelepathyService", appUuid)
                Log.d("Bluetooth", "Advertising started...")
                while (isAdvertising) {
                    val socket = serverSocket?.accept()
                    socket?.let {
                        Log.d("Bluetooth", "Connection accepted from ${it.remoteDevice.name}")

                        sendUser(it, localUser)

                        startCommunication(it)
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

        val discoveredDevices = mutableSetOf<String>() // Używamy zbioru, aby unikać duplikatów.
        val targetUuid = UUID.fromString("12345678-1234-5678-1234-567812345678") // UUID Twojej aplikacji.

        val discoveryReceiver = BluetoothDiscoveryReceiver(
            onDeviceFound = { device ->
                val name = device.name ?: "Unknown Device"
                val address = device.address
                Log.d("Bluetooth", "Discovered device: $name, $address")

                // Wywołanie fetchUuidsWithSdp dla znalezionego urządzenia.
                device.fetchUuidsWithSdp()
            },
            onUuidFetched = { device, uuids ->
                Log.d("Bluetooth", "Fetched UUIDs for device ${device.name}: $uuids")

                // Sprawdź, czy urządzenie obsługuje UUID aplikacji.
                if (uuids.contains(targetUuid)) {
                    CoroutineScope(Dispatchers.Main).launch {
                        discoveredDevices.add(device.address)
                        _discoveredUsers.value = discoveredDevices.toList()
                    }
                    Log.d("Bluetooth", "Device ${device.name} matches the app's UUID!")
                }
            }
        )

        // Rejestracja odbiornika
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
