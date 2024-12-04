package com.example.telepathy.ui.users

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.telepathy.clases.User
import androidx.compose.ui.graphics.Color

class UserViewModel : ViewModel() {

    var localUser by mutableStateOf(
        User(
            id = 0,
            name = "Locales",
            color = Color(0xFF4682B4),
            avatar = null,  // You can load the avatar if you have one
            chatHistory = mutableListOf(),
            isLocalUser = true,
            description = "Jestem lokalsem"
        )
    )

    // Optionally, you can add methods to update the user data, if needed.
    fun updateUserName(newName: String) {
        localUser = localUser.copy(name = newName)
    }

    // Other methods to update user data...
}
