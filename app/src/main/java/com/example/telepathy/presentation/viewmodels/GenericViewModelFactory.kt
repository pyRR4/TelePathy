package com.example.telepathy.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.telepathy.data.AppDatabase
import com.example.telepathy.data.repositories.MessageRepositoryImpl
import com.example.telepathy.data.repositories.UserRepositoryImpl
import com.example.telepathy.domain.bluetooth.BluetoothRepository

class GenericViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    private val database by lazy { AppDatabase.getDatabase(context) }
    private val userRepository by lazy { UserRepositoryImpl(userDao = database.userDao()) }
    private val messageRepository by lazy { MessageRepositoryImpl(messageDao = database.messageDao()) }
    private val bluetoothRepository by lazy { BluetoothRepository(context) }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AvailableViewModel::class.java) -> {
                AvailableViewModel(bluetoothRepository, userRepository) as T
            }
            modelClass.isAssignableFrom(ChatViewModel::class.java) -> {
                ChatViewModel(userRepository, messageRepository) as T
            }
            modelClass.isAssignableFrom(ContactsViewModel::class.java) -> {
                ContactsViewModel(userRepository, messageRepository) as T
            }
            modelClass.isAssignableFrom(SharedViewModel::class.java) -> {
                SharedViewModel(userRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
