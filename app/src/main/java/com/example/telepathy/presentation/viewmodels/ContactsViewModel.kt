package com.example.telepathy.presentation.viewmodels

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
            userRepository.getAllUsers()
                .collect { users ->
                    users
                        .filter {
                            it.id != localUserId
                        }.forEach { user ->
                            launch {
                                messageRepository.getLastMessage(user.id, localUserId).collect { message ->
                                    _contacts.value = _contacts.value + (user.toDTO() to message)
                                }
                            }
                    }
                }
        }
    }

    fun removeContact(contact: User) {
        viewModelScope.launch {
            userRepository.delete(contact)
        }
    }
}
