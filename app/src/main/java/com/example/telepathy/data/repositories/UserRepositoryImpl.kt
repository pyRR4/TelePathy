package com.example.telepathy.data.repositories

import com.example.telepathy.data.daos.ContactDao
import com.example.telepathy.data.daos.UserDao
import com.example.telepathy.data.entities.Contact
import com.example.telepathy.data.entities.User
import com.example.telepathy.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val userDao: UserDao,
    private val contactDao: ContactDao
) : UserRepository {
    override fun getUser(id: Int): Flow<User> = userDao.getUser(id)

    override suspend fun insert(user: User) = userDao.insert(user)

    override suspend fun update(user: User) = userDao.update(user)

    override suspend fun delete(user: User) = userDao.delete(user)

    override suspend fun addContact(contact: Contact) {
        contactDao.insertContact(contact)
    }

    override suspend fun removeContact(contact: Contact) {
        contactDao.deleteContact(contact)
    }

    override fun getContactsForUser(userId: Int): Flow<List<Contact>> {
        return contactDao.getContactsForUser(userId)
    }

    override fun getUserByDeviceId(deviceId: String): Flow<User> {
        return userDao.getUserByDeviceId(deviceId)
    }
}