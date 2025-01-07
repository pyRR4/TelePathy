package com.example.telepathy.data.converters

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter

class ColorConverter {

    @TypeConverter
    fun fromColor(color: Color?): Long? {
        val colorValue = color?.let {
            ((it.alpha * 255).toInt().toLong() shl 24) or
                    ((it.red * 255).toInt().toLong() shl 16) or
                    ((it.green * 255).toInt().toLong() shl 8) or
                    (it.blue * 255).toInt().toLong()
        }
        Log.d("ColorConverter", "Converting Color to Long: $color -> $colorValue")
        return colorValue
    }

    @TypeConverter
    fun toColor(colorValue: Long?): Color {
        val color = colorValue?.let {
            val alpha = ((it shr 24) and 0xFF).toFloat() / 255
            val red = ((it shr 16) and 0xFF).toFloat() / 255
            val green = ((it shr 8) and 0xFF).toFloat() / 255
            val blue = (it and 0xFF).toFloat() / 255
            Color(red, green, blue, alpha)
        } ?: Color.Gray
        Log.d("ColorConverter", "Converting Long to Color: $colorValue -> $color")
        return color
    }
}


