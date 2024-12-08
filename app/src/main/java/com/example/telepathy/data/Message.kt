package com.example.telepathy.data

data class Message(
    val sender: String,
    val content: String,
    val timestamp: Long,
    val fromLocalUser: Boolean
)
