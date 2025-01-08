package com.example.telepathy.domain.bluetooth

import android.bluetooth.BluetoothSocket
import android.util.Log
import com.example.telepathy.domain.serialization.deserializeUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun receiveData(socket: BluetoothSocket) {
    CoroutineScope(Dispatchers.IO).launch {
        val inputStream = socket.inputStream
        val buffer = ByteArray(1024)
        var bytesRead: Int

        try {
            while (true) {
                bytesRead = inputStream.read(buffer)
                val message = String(buffer, 0, bytesRead)
                Log.d("Bluetooth", "Received message: $message")

                when {
                    message.startsWith("USER:") -> {
                        val userJson = message.removePrefix("USER:")
                        val user = deserializeUser(userJson)
                        Log.d("Bluetooth", "User received: $user")
                        // Obsłuż dane użytkownika
                    }

                    message.startsWith("MSG:") -> {
                        val textMessage = message.removePrefix("MSG:")
                        Log.d("Bluetooth", "Message received: $textMessage")
                        // Obsłuż wiadomość tekstową
                        handleReceivedMessage(textMessage)
                    }

                    else -> {
                        Log.e("Bluetooth", "Unknown message type")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("Bluetooth", "Error during data reception", e)
        }
    }
}

fun handleReceivedMessage(message: String) {

    Log.d("Bluetooth", "Handling received message: $message")
}


fun sendMessage(socket: BluetoothSocket, message: String) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val outputStream = socket.outputStream
            outputStream.write(message.toByteArray())
            Log.d("Bluetooth", "Message sent: $message")
        } catch (e: Exception) {
            Log.e("Bluetooth", "Failed to send message", e)
        }
    }
}