package com.example.telepathy.data.converters

import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter

class ColorConverter {
    @TypeConverter
    fun fromColor(color: Color?): Long? {
        val alpha = ((color?.alpha ?: 0f) * 255).toInt() and 0xFF // Alpha channel (8 bits)
        val red = ((color?.red ?: 0f) * 255).toInt() and 0xFF     // Red channel (8 bits)
        val green = ((color?.green ?: 0f) * 255).toInt() and 0xFF // Green channel (8 bits)
        val blue = ((color?.blue ?: 0f) * 255).toInt() and 0xFF   // Blue channel (8 bits)

        return (alpha.toLong() shl 24) or
                (red.toLong() shl 16) or
                (green.toLong() shl 8) or
                blue.toLong()
    }

    @TypeConverter
    fun toColor(colorValue: Long?): Color? {
        return colorValue?.let { Color(it) }
    }

}