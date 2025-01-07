package com.example.telepathy.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telepathy.data.entities.User
import com.example.telepathy.data.repositories.BluetoothRepository
import com.example.telepathy.domain.repositories.UserRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.telepathy.data.AppDatabase
import com.example.telepathy.data.repositories.UserRepositoryImpl

class AvailableViewModel(
    private val bluetoothRepository: BluetoothRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val discoveredUsers: StateFlow<List<User>> = bluetoothRepository.discoveredUsers

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