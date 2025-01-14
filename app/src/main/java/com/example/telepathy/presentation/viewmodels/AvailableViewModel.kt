package com.example.telepathy.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telepathy.domain.bluetooth.BluetoothRepository
import com.example.telepathy.domain.repositories.UserRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.telepathy.domain.dtos.UserDTO
import com.example.telepathy.domain.mappers.UserMapper.toEntity

class AvailableViewModel(
    private val bluetoothRepository: BluetoothRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val discoveredUsersDeviceIds: StateFlow<List<UserDTO>> = bluetoothRepository.discoveredUsers

    fun startScan() {
        bluetoothRepository.startScan()
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

    fun addUserToLocalContacts(user: UserDTO) {
        viewModelScope.launch {
            userRepository.insert(user.toEntity())
        }
    }
}