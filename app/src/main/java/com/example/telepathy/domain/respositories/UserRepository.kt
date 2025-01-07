package com.example.telepathy.domain.repositories

import com.example.telepathy.data.entities.Contact
import com.example.telepathy.data.entities.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUser(id: Int): Flow<User>

    suspend fun insert(user: User)

    suspend fun update(user: User)

    suspend fun delete(user: User)

    suspend fun addContact(contact: Contact)

    suspend fun removeContact(contact: Contact)

    fun getContactsForUser(userId: Int): Flow<List<Contact>>
}
