package com.example.telepathy.clases

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import android.content.Context
import android.graphics.BitmapFactory

data class User(
    val name: String,
    val color: Color,
    var avatar: Bitmap? = null,
    val chatHistory: MutableList<Message> = mutableListOf(),
    val isLocalUser: Boolean = false
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
        this.avatar = BitmapFactory.decodeResource(context.resources, resId)
    }
}
