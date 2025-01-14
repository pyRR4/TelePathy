package com.example.telepathy.domain.mappers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.Color
import com.example.telepathy.data.entities.User
import com.example.telepathy.domain.dtos.UserDTO
import java.io.ByteArrayOutputStream

object UserMapper {
    fun User.toDTO(): UserDTO {
        return UserDTO(
            id = this.id,
            name = this.name,
            description = this.description,
            avatar = this.avatar?.toBitmap(),
            color = this.color.toColor(),
            deviceId = this.deviceId
        )
    }

    fun UserDTO.toEntity(): User {
        return User(
            id = this.id,
            name = this.name,
            description = this.description,
            avatar = this.avatar?.toByteArray(),
            color = this.color.toLong(),
            deviceId = this.deviceId
        )
    }

    fun ByteArray?.toBitmap(): Bitmap? = this?.let {
            BitmapFactory.decodeByteArray(it, 0, it.size)
        }

    fun Bitmap?.toByteArray(): ByteArray? {
        val outputStream = ByteArrayOutputStream()

        this?.let {
            val scaledBitmap = scaleDownBitmap(it, 1000, 1000)
            scaledBitmap.compress(Bitmap.CompressFormat.WEBP, 80, outputStream)
        }
        return outputStream.toByteArray()
    }

    fun scaleDownBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val aspectRatio = bitmap.width.toFloat() / bitmap.height
        val width = if (aspectRatio > 1) maxWidth else (maxHeight * aspectRatio).toInt()
        val height = if (aspectRatio > 1) (maxWidth / aspectRatio).toInt() else maxHeight
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }

    fun Color.toLong(): Long {
        val alpha = ((this.alpha) * 255).toInt() and 0xFF
        val red = ((this.red) * 255).toInt() and 0xFF
        val green = ((this.green) * 255).toInt() and 0xFF
        val blue = ((this.blue) * 255).toInt() and 0xFF

        return (alpha.toLong() shl 24) or
                (red.toLong() shl 16) or
                (green.toLong() shl 8) or
                blue.toLong()
    }

    fun Long.toColor(): Color {
        return Color(this)
    }

}