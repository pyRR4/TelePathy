package com.example.telepathy.domain.repositories

import com.example.telepathy.data.entities.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun insert(message: Message)
    suspend fun delete(message: Message)
    fun getChatHistory(userId: Int): Flow<List<Message>>
    fun getLastMessage(userId: Int, contactId: Int): Flow<Message>
}