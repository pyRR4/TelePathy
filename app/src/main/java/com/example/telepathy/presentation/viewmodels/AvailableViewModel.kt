package com.example.telepathy.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telepathy.data.entities.User
import com.example.telepathy.domain.bluetooth.BluetoothRepository
import com.example.telepathy.domain.repositories.UserRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.telepathy.data.AppDatabase
import com.example.telepathy.data.repositories.UserRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.forEach

class AvailableViewModel(
    private val bluetoothRepository: BluetoothRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val discoveredUsersDeviceIds: StateFlow<List<User>> = bluetoothRepository.discoveredUsers

    private val _discoveredUsers = MutableStateFlow<List<User>>(emptyList())
    val discoveredUsers: StateFlow<List<User>> = _discoveredUsers.asStateFlow()

    private val _localUser = MutableStateFlow<User?>(null)
    val localUser: StateFlow<User?> = _localUser.asStateFlow()

    fun loadUsers() {
        viewModelScope.launch {
            discoveredUsersDeviceIds.collect { users ->
                users.forEach { user ->
                        val updatedList = _discoveredUsers.value.toMutableList()
                        updatedList.add(user)
                        _discoveredUsers.value = updatedList
                }
            }
        }
    }

    fun loadLocalUser(userId: Int) {
        viewModelScope.launch {
            userRepository.getUser(userId)
                .catch { e ->

                }
                .collect { user ->
                    _localUser.value = user
                }
        }
    }


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

    fun addUserToLocalContacts(user: User) {
        viewModelScope.launch {
            userRepository.insert(user)
        }
    }
}

class AvailableViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val database = AppDatabase.getDatabase(context)

        val userRepositoryInstance = UserRepositoryImpl(
            userDao = database.userDao(),
            contactDao = database.contactDao()
        )

        val bluetoothRepositoryInstance = BluetoothRepository(context)

        if (modelClass.isAssignableFrom(AvailableViewModel::class.java)) {
            return AvailableViewModel(bluetoothRepositoryInstance, userRepositoryInstance) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}