package com.example.telepathy.data.repositories

import com.example.telepathy.data.daos.UserDao
import com.example.telepathy.data.entities.User
import com.example.telepathy.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {
    override fun getUser(id: Int): Flow<User> = userDao.getUser(id)

    override fun getAllUsers(): Flow<List<User>> = userDao.getAllUsers()

    override suspend fun insert(user: User) = userDao.insert(user)

    override suspend fun update(user: User) = userDao.update(user)

    override suspend fun delete(user: User) = userDao.delete(user)

    override fun getUserByDeviceId(deviceId: String): Flow<User> {
        return userDao.getUserByDeviceId(deviceId)
    }
}