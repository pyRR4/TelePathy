package com.example.telepathy.data

import android.content.Context
import androidx.compose.ui.graphics.Color
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.telepathy.data.entities.User

object LocalPreferences {
    var localUser: User? = null
    var PIN : String = "0000"

    fun loadLocalUser(
        id: Int,
        name: String,
        color: Color,
        description: String,
        context: Context,
        avatarResId: Int? = null
    ) {
        val avatar: Bitmap? = avatarResId?.let {
            BitmapFactory.decodeResource(context.resources, it)
        }
        localUser = User(
            id = id,
            name = name,
            color = color,
            avatar = avatar,
            description = description
        )
    }

    fun createBasicLocalUser(context: Context) {
        if (localUser == null) {
            localUser = User(
                id = 0,
                name = "Guest",
                color = Color(0xFFE57373), // 1 color in table - red
                avatar = null,
                description = "Set your descreption"
            )
        }
    }

    fun clearLocalUser() {
        localUser = null
    }


}
