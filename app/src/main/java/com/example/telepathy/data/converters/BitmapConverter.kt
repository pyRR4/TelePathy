package com.example.telepathy.data.converters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class BitmapConverter {
    @TypeConverter
    fun fromBitmap(bitmap: Bitmap?): ByteArray? {
        val outputStream = ByteArrayOutputStream()

        bitmap?.let {
            val scaledBitmap = scaleDownBitmap(it, 1000, 1000)
            scaledBitmap.compress(Bitmap.CompressFormat.WEBP, 80, outputStream)
        }
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray?): Bitmap? {
        return byteArray?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
    }

    fun scaleDownBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val aspectRatio = bitmap.width.toFloat() / bitmap.height
        val width = if (aspectRatio > 1) maxWidth else (maxHeight * aspectRatio).toInt()
        val height = if (aspectRatio > 1) (maxWidth / aspectRatio).toInt() else maxHeight
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }

}