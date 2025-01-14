package com.example.telepathy.presentation.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.telepathy.data.AppDatabase
import com.example.telepathy.data.entities.Message
import com.example.telepathy.data.entities.User
import com.example.telepathy.data.repositories.MessageRepositoryImpl
import com.example.telepathy.data.repositories.UserRepositoryImpl
import com.example.telepathy.domain.repositories.MessageRepository
import com.example.telepathy.domain.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContactsViewModel(
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository
) : ViewModel() {

    private val _contacts = MutableStateFlow<Map<User, Message?>>(emptyMap())
    val contacts: StateFlow<Map<User, Message?>> = _contacts.asStateFlow()

    fun loadContacts(localUserId: Int) {
        viewModelScope.launch {
            Log.d("ContactsViewModel", "Loading contacts for localUserId: $localUserId")

            val contactsMap = mutableMapOf<User, Message?>()
            userRepository.getAllUsers()
                .collect { users ->
                    Log.d("ContactsViewModel", "Fetched users: ${users.size}")
                    users
                        .filter { it.id != localUserId }
                        .forEach { user ->
                            Log.d("ContactsViewModel", "Processing user: ${user.name}, ID: ${user.id}")

                            launch {
                                messageRepository.getLastMessage(user.id, localUserId)
                                    .collect { message ->
                                        Log.d(
                                            "ContactsViewModel",
                                            "Received last message for user: ${user.name}, ID: ${user.id}, Message: ${message?.content}"
                                        )
                                        contactsMap[user] = message
                                        _contacts.value = contactsMap.toMap()
                                        Log.d("ContactsViewModel", "Updated contacts map: ${_contacts.value}")
                                    }
                            }
                        }
                }
        }
    }

}
