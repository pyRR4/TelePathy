package com.example.telepathy.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telepathy.data.entities.Contact
import com.example.telepathy.data.entities.Message
import com.example.telepathy.data.entities.User
import com.example.telepathy.domain.repositories.MessageRepository
import com.example.telepathy.domain.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ContactsViewModel(
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository
) : ViewModel() {

    private val _contacts = MutableStateFlow<Map<User, Message>>(emptyMap())
    val contacts: StateFlow<Map<User, Message>> = _contacts.asStateFlow()

    fun loadContacts(userId: Int) {
        viewModelScope.launch {
            userRepository.getContactsForUser(userId)
                .collect { contacts -> 
                    contacts.forEach { contactList ->
                        val id = if (contactList.userId == userId)
                            contactList.contactId else contactList.userId
                        launch {
                            userRepository.getUser(id).collect { user ->
                                messageRepository.getLastMessage(userId, id).collect { message ->
                                    _contacts.value = _contacts.value + (user to message)
                                }
                            }
                        }
                    }
                }
        }
    }

    fun removeContact(contact: Contact) {
        viewModelScope.launch {
            userRepository.removeContact(contact)
        }
    }
}