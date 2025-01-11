package com.example.telepathy.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telepathy.data.entities.User
import com.example.telepathy.domain.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SharedViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _localUser = MutableStateFlow<User?>(null)
    val localUser: StateFlow<User?> = _localUser

    fun loadLocalUser(localUserId: Int) {
        viewModelScope.launch {
            userRepository.getUser(localUserId).collect { user ->
                _localUser.value = user
            }
        }
    }

    fun updateLocalUser(updatedUser: User) {
        viewModelScope.launch {
            userRepository.update(updatedUser)
            _localUser.value = updatedUser
        }
    }
}
