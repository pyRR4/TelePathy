package com.example.telepathy.domain.dtos

import android.graphics.Bitmap

data class UserDTO(
    val id: Int,
    val name: String,
    val description: String,
    val avatar: Bitmap?,
    val color: androidx.compose.ui.graphics.Color,
    val deviceId: String
)
