package com.example.telepathy.presentation.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.telepathy.data.AppDatabase
import com.example.telepathy.data.entities.User
import com.example.telepathy.data.repositories.UserRepositoryImpl
import com.example.telepathy.domain.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    fun loadUser(localUserId: Int) {
        viewModelScope.launch {
            userRepository.getUser(localUserId).collect { user ->
                _user.value = user
            }
        }
    }

    fun updateUser(updatedUser: User) {
        Log.d("UserUpdate", "Updating user: ${updatedUser.id}, Name: ${updatedUser.name}")

        viewModelScope.launch {
            try {
                userRepository.update(updatedUser)
                Log.d("UserUpdate", "User updated successfully: ${updatedUser.id}")
            } catch (e: Exception) {
                Log.e("UserUpdate", "Error updating user: ${updatedUser.id}, Error: ${e.message}")
            }
        }
    }

    class EditProfileViewModelFactory(
        context: Context
    ) : ViewModelProvider.Factory {
        private val database = AppDatabase.getDatabase(context)

        val userRepositoryInstance = UserRepositoryImpl(
            userDao = database.userDao()
        )

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
                return EditProfileViewModel(userRepositoryInstance) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
