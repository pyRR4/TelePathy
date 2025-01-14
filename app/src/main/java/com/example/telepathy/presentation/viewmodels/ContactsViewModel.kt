package com.example.telepathy.presentation.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telepathy.data.entities.Message
import com.example.telepathy.data.entities.User
import com.example.telepathy.domain.dtos.UserDTO
import com.example.telepathy.domain.mappers.UserMapper.toDTO
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

    private val _contacts = MutableStateFlow<Map<UserDTO, Message?>>(emptyMap())
    val contacts: StateFlow<Map<UserDTO, Message?>> = _contacts.asStateFlow()

    fun loadContacts(localUserId: Int) {
        viewModelScope.launch {
            Log.d("ContactsViewModel", "Loading contacts for localUserId: $localUserId")

            val contactsMap = mutableMapOf<UserDTO, Message?>()
            userRepository.getAllUsers()
                .collect { users ->
                    Log.d("ContactsViewModel", "Fetched users: ${users.size}")
                    users
                        .filter { it.id != localUserId }
                        .forEach { user ->
                            val userDTO = user.toDTO()
                            Log.d("ContactsViewModel", "Processing user: ${userDTO.name}, ID: ${userDTO.id}")

                            launch {
                                messageRepository.getLastMessage(userDTO.id, localUserId)
                                    .collect { message ->
                                        Log.d(
                                            "ContactsViewModel",
                                            "Received last message for user: ${userDTO.name}, ID: ${userDTO.id}, Message: ${message?.content}"
                                        )
                                        contactsMap[userDTO] = message

                                        // Sortowanie kontaktów po dacie ostatniej wiadomości
                                        val sortedContacts = contactsMap.entries
                                            .sortedByDescending { it.value?.timestamp ?: 0L }
                                            .associate { it.key to it.value }

                                        _contacts.value = sortedContacts
                                        Log.d("ContactsViewModel", "Updated contacts map: ${_contacts.value}")
                                    }
                            }
                        }
                }
        }
    }

}
