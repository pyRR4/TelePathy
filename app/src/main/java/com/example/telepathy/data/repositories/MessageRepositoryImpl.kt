package com.example.telepathy.data.repositories

import com.example.telepathy.data.daos.MessageDao
import com.example.telepathy.data.entities.Message
import com.example.telepathy.domain.repositories.MessageRepository
import kotlinx.coroutines.flow.Flow

class MessageRepositoryImpl(
    private val messageDao: MessageDao
) : MessageRepository {

    override suspend fun insert(message: Message) {
        messageDao.insert(message)
    }

    override suspend fun delete(message: Message) {
        messageDao.delete(message)
    }

    override fun getChatHistory(userId: Int): Flow<List<Message>> {
        return messageDao.getChatHistory(userId)
    }

    override suspend fun getLastMessage(userId: Int, contactId: Int): Flow<Message?> {
        return messageDao.getLastMessage(userId, contactId)
    }
}