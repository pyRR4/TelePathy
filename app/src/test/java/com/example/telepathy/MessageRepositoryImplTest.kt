package com.example.telepathy.data.repositories

import com.example.telepathy.data.daos.MessageDao
import com.example.telepathy.data.entities.Message
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.*

class MessageRepositoryImplTest {

    private val messageDao = mock(MessageDao::class.java)
    private val repository = MessageRepositoryImpl(messageDao)

    @Test
    fun `getChatHistory should return list of messages`() = runBlocking {
        val messages = listOf(
            Message(1, "Hello", 1, 2, 12345L),
            Message(2, "Hi", 2, 1, 12346L)
        )
        `when`(messageDao.getChatHistory(1)).thenReturn(flowOf(messages))

        val result = repository.getChatHistory(1).first()

        assertEquals(messages, result)
    }

    @Test
    fun `insert should call MessageDao insert`() = runBlocking {
        val message = Message(1, "Hello", 1, 2, 12345L)
        repository.insert(message)

        verify(messageDao, times(1)).insert(message)
    }

    @Test
    fun `delete should call MessageDao delete`() = runBlocking {
        val message = Message(1, "Hello", 1, 2, 12345L)
        repository.delete(message)

        verify(messageDao, times(1)).delete(message)
    }
}
