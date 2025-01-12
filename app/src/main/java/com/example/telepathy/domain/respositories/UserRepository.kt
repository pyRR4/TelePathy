package com.example.telepathy.domain.repositories

import com.example.telepathy.data.entities.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUser(id: Int): Flow<User>

    fun getAllUsers(): Flow<List<User>>

    suspend fun insert(user: User)

    suspend fun update(user: User)

    suspend fun delete(user: User)

    fun getUserByDeviceId(deviceId: String): Flow<User>
}
