package com.example.telepathy.data

import android.content.Context
import androidx.compose.ui.graphics.Color
import android.graphics.Bitmap
import android.graphics.BitmapFactory

object LocalPreferences {
    var localUser: User? = null

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
            isLocalUser = true,
            description = description
        )
    }

    fun clearLocalUser() {
        localUser = null
    }
}
