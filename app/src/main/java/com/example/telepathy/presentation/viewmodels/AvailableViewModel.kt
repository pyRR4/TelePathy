package com.example.telepathy.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telepathy.domain.bluetooth.BluetoothRepository
import com.example.telepathy.domain.repositories.UserRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.telepathy.domain.dtos.UserDTO
import com.example.telepathy.domain.mappers.UserMapper.toEntity
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import java.lang.IllegalStateException

class AvailableViewModel(
    private val bluetoothRepository: BluetoothRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val discoveredUsers: StateFlow<Set<UserDTO>> = bluetoothRepository.discoveredUsers

    private val _filteredUsers = MutableStateFlow<Set<UserDTO>>(discoveredUsers.value)
    val filteredUsers: StateFlow<Set<UserDTO>> = _filteredUsers

    val isDiscoverable: StateFlow<Boolean> = bluetoothRepository.isDiscoverable

    fun startScan(localUser: UserDTO) {
        bluetoothRepository.startScan(localUser.toEntity())
    }

    fun stopScan() {
        bluetoothRepository.stopScan()
    }

    fun startAdvertising(localUser: UserDTO) {
        bluetoothRepository.startAdvertising(localUser.toEntity())
    }

    fun stopAdvertising() {
        bluetoothRepository.stopAdvertising()
    }
//    fun addUserToLocalContacts(user: User) {
//        viewModelScope.launch {
//            userRepository.insert(user)
//        }
//    }
    fun addUserToLocalContacts(user: UserDTO) {
        Log.d("Contacts", "Attempting to add user: ${user.name}, ID: ${user.id}")

        viewModelScope.launch {
            try {
                userRepository.insert(user.toEntity())
                filterDiscoveredUsers()
                Log.d("avialable", "userRepository.insert(user) with no exception ${user.name}, ID: ${user.id}")
            } catch (e: Exception) {
                Log.e("avialable", "userRepository.insert(user) Failed to add user: ${user.name}, ID: ${user.id}", e)
            }
        }
    }

    suspend fun filterDiscoveredUsers() {
        val allDiscoveredUsers = discoveredUsers.value


        val newUsers = allDiscoveredUsers.filter { user ->
            Log.d("USER TO FILTER: ", "$user")
            try {
                val existingUser = userRepository.getUserByDeviceId(user.deviceId).first()
                Log.d("Existing user: ", "Existing user: ${existingUser}")
                existingUser == null
            } catch (e: IllegalStateException) {
                true
            }
        }.toSet()

        _filteredUsers.value = newUsers
    }
}