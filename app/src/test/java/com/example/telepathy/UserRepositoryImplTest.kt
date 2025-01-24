package com.example.telepathy

import com.example.telepathy.data.daos.UserDao
import com.example.telepathy.data.entities.User
import com.example.telepathy.data.repositories.UserRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.*

class UserRepositoryImplTest {

    private val userDao = mock(UserDao::class.java)
    private val repository = UserRepositoryImpl(userDao)

    @Test
    fun `getUser should return correct user`() = runBlocking {
        val user = User(1, "John Doe", "Test user", 123456, null, "device123")
        `when`(userDao.getUser(1)).thenReturn(flowOf(user))

        val result = repository.getUser(1).first()

        assertEquals(user, result)
    }

    @Test
    fun `insert should call UserDao insert`() = runBlocking {
        val user = User(1, "John Doe", "Test user", 123456, null, "device123")
        repository.insert(user)

        verify(userDao, times(1)).insert(user)
    }

    @Test
    fun `delete should call UserDao delete`() = runBlocking {
        val user = User(1, "John Doe", "Test user", 123456, null, "device123")
        repository.delete(user)

        verify(userDao, times(1)).delete(user)
    }
}
