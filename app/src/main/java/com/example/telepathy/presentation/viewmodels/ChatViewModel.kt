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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel (
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository
) : ViewModel() {

    private val _chatHistory = MutableStateFlow<List<Message>>(emptyList())
    val chatHistory: StateFlow<List<Message>> = _chatHistory.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts.asStateFlow()

    fun loadChatHistory(localUserId: Int, remoteUserId: Int) {
        viewModelScope.launch {
            messageRepository.getChatHistory(localUserId)
                .map { messages ->
                    messages.filter {
                        (it.senderId == localUserId && it.recipientId == remoteUserId) ||
                                (it.senderId == remoteUserId && it.recipientId == localUserId)
                    }
                }
                .catch { e ->

                }
                .collect { filteredMessages ->
                    _chatHistory.value = filteredMessages
                }
        }
    }

    fun loadUser(userId: Int) {
        viewModelScope.launch {
            userRepository.getUser(userId)
                .catch { e ->

                }
                .collect { user ->
                    _currentUser.value = user
                }
        }
    }

    fun sendMessage(message: Message) {
        viewModelScope.launch {
            try {
                messageRepository.insert(message)
                _chatHistory.update { it + message }
            } catch (e: Exception) {

            }
        }
    }

    fun deleteMessage(message: Message) {
        viewModelScope.launch {
            try {
                messageRepository.delete(message)
                _chatHistory.update { it.filter { it.id != message.id } }
            } catch (e: Exception) {

            }
        }
    }

    fun addContact(userId: Int, contactId: Int) {
        viewModelScope.launch {
            val contact = Contact(id = 0, userId = userId, contactId = contactId)
            userRepository.addContact(contact)
        }
    }
}