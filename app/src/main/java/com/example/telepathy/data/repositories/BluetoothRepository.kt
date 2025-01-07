package com.example.telepathy.data.repositories

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.telepathy.data.entities.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

class BluetoothRepository(private val context: Context) {

    private val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private val _discoveredUsers = MutableStateFlow<List<User>>(emptyList())
    val discoveredUsers: StateFlow<List<User>> = _discoveredUsers

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
        bluetoothAdapter.startDiscovery()
        val discoveryReceiver = BluetoothDiscoveryReceiver { device ->
            Log.d("Bluetooth", "Discovered device: ${device.name}, ${device.address}")
            // Dodaj logikę dodawania użytkownika do listy discoveredUsers
        }
        context.registerReceiver(discoveryReceiver, BluetoothDiscoveryReceiver.filter)
    }

    @SuppressLint("MissingPermission")
    fun stopScan() {
        bluetoothAdapter.cancelDiscovery()
        Log.d("Bluetooth", "Scanning stopped.")
    }

    private fun startCommunication(socket: BluetoothSocket) {
        val inputStream: InputStream = socket.inputStream
        val outputStream: OutputStream = socket.outputStream

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

        fun sendMessage(message: String) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    outputStream.write(message.toByteArray())
                    Log.d("Bluetooth", "Message sent: $message")
                } catch (e: Exception) {
                    Log.e("Bluetooth", "Failed to send message", e)
                }
            }
        }
    }
}
