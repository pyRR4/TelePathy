package com.example.telepathy.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["senderId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["recipientId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("senderId"), Index("recipientId")]
)
data class Message (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val content: String,

    val senderId: Int,

    val recipientId: Int,

    val timestamp: Long
)