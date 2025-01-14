package com.example.telepathy.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [androidx.room.Index(value = ["deviceId"], unique = true)]
)
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,

    var description: String,

    val color: Long,

    val avatar: ByteArray? = null,

    val deviceId: String
)