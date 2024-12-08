package com.example.telepathy.data

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import android.content.Context
import android.graphics.BitmapFactory

data class User(
    val id: Int,
    val name: String,
    val color: Color,
    var avatar: Bitmap? = null,
    val chatHistory: MutableList<Message> = mutableListOf(),
    val isLocalUser: Boolean = false,
    var description: String
) {
    fun addMessage(message: Message) {
        chatHistory.add(message)
    }

    fun clearChat() {
        chatHistory.clear()
    }

    fun updateAvatar(newAvatar: Bitmap) {
        avatar = newAvatar
    }

    fun loadAvatarFromResources(context: Context, resId: Int) {
        avatar = BitmapFactory.decodeResource(context.resources, resId)
    }

    fun loadAvatarFromFile(filePath: String) {
        avatar = BitmapFactory.decodeFile(filePath)
    }
}
