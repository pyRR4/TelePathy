package com.example.telepathy.data.converters

import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter

class ColorConverter {
    @TypeConverter
    fun fromColor(color: Color?): Long? {
        return color?.value?.toLong()
    }

    @TypeConverter
    fun toColor(colorValue: Long?): Color? {
        return colorValue?.let { Color(it) }
    }

}