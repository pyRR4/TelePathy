package com.example.telepathy.data.repositories

import com.example.telepathy.data.entities.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BluetoothRepository {

    private val _discoveredUsers = MutableStateFlow<List<User>>(emptyList())
    val discoveredUsers: StateFlow<List<User>> = _discoveredUsers.asStateFlow()

    fun startScan() {
        _discoveredUsers.value = listOf(
            User(name = "Alice", id = 101, description = "Around you", color = androidx.compose.ui.graphics.Color.Magenta),
            User(name = "Bob", id = 202, description = "Nearby buddy", color = androidx.compose.ui.graphics.Color.Cyan)
        )
    }

    fun stopScan() {
        _discoveredUsers.value = emptyList()
    }

    fun startAdvertising(localUser: User) {
        // W rzeczywistej aplikacji ustawisz widoczność Bluetooth i reklamę użytkownika
    }

    fun stopAdvertising() {
        // Zatrzymaj reklamę użytkownika
    }
}
