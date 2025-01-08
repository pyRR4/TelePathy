package com.example.telepathy.presentation.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.telepathy.data.AppDatabase
import com.example.telepathy.data.entities.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditProfileViewModel(context: Context, private val localUserId: Int) : ViewModel() {

    private val database = AppDatabase.getDatabase(context)
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            database.userDao().getUser(localUserId).collect { user ->
                _user.value = user
            }
        }
    }

    fun updateUser(updatedUser: User) {
        Log.d("UserUpdate", "Updating user: ${updatedUser.id}, Name: ${updatedUser.name}")

        viewModelScope.launch {
            try {
                database.userDao().update(updatedUser)
                Log.d("UserUpdate", "User updated successfully: ${updatedUser.id}")
            } catch (e: Exception) {
                Log.e("UserUpdate", "Error updating user: ${updatedUser.id}, Error: ${e.message}")
            }
        }
    }

    class EditProfileViewModelFactory(
        private val context: Context,
        private val localUserId: Int
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
                return EditProfileViewModel(context, localUserId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
