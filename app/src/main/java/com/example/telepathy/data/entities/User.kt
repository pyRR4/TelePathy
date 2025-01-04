package com.example.telepathy.data.entities

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.telepathy.data.converters.BitmapConverter
import com.example.telepathy.data.converters.ColorConverter

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,

    var description: String,

    @field:TypeConverters(ColorConverter::class)
    val color: Color,

    @field:TypeConverters(BitmapConverter::class)
    val avatar: Bitmap? = null
)