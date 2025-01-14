package com.example.telepathy.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telepathy.data.entities.User
import com.example.telepathy.domain.bluetooth.BluetoothRepository
import com.example.telepathy.domain.repositories.UserRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.telepathy.data.AppDatabase
import com.example.telepathy.data.repositories.UserRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch

class AvailableViewModel(
    private val bluetoothRepository: BluetoothRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val discoveredUsersDeviceIds: StateFlow<List<User>> = bluetoothRepository.discoveredUsers

    fun startScan() {
        bluetoothRepository.startScan()
    }

    fun stopScan() {
        bluetoothRepository.stopScan()
    }

    fun startAdvertising(localUser: User) {
        bluetoothRepository.startAdvertising(localUser)
    }

    fun stopAdvertising() {
        bluetoothRepository.stopAdvertising()
    }

//    fun addUserToLocalContacts(user: User) {
//        viewModelScope.launch {
//            userRepository.insert(user)
//        }
//    }
    fun addUserToLocalContacts(user: User) {
        Log.d("Contacts", "Attempting to add user: ${user.name}, ID: ${user.id}")

        viewModelScope.launch {
            try {
                userRepository.insert(user)
                Log.d("avialable", "userRepository.insert(user) with no exception ${user.name}, ID: ${user.id}")
            } catch (e: Exception) {
                Log.e("avialable", "userRepository.insert(user) Failed to add user: ${user.name}, ID: ${user.id}", e)
            }
        }
    }
}