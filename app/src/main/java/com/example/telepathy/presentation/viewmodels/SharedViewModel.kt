package com.example.telepathy.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telepathy.data.entities.User
import com.example.telepathy.domain.dtos.UserDTO
import com.example.telepathy.domain.mappers.UserMapper.toDTO
import com.example.telepathy.domain.mappers.UserMapper.toEntity
import com.example.telepathy.domain.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SharedViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _localUser = MutableStateFlow<UserDTO?>(null)
    val localUser: StateFlow<UserDTO?> = _localUser

    fun loadLocalUser(localUserId: Int) {
        viewModelScope.launch {
            userRepository.getUser(localUserId).collect { user ->
                _localUser.value = user.toDTO()
            }
        }
    }

    fun updateLocalUser(updatedUser: UserDTO) {
        viewModelScope.launch {
            val userEntity = updatedUser.toEntity()
            userRepository.update(userEntity)
            _localUser.value = updatedUser
        }
    }

    fun createTestUser(): StateFlow<User?> {
        val testUserFlow = MutableStateFlow<User?>(null)
        viewModelScope.launch {
            val testUser = User(
                id = 0,
                name = "Test User",
                description = "This is a test user",
                color = 123456,
                avatar = null,
                deviceId = "testDeviceId"
            )
            userRepository.insert(testUser)

            userRepository.getAllUsers().collect { users ->
                val insertedUser = users.find { it.deviceId == "testDeviceId" }
                testUserFlow.value = insertedUser
            }
        }
        return testUserFlow
    }

}
