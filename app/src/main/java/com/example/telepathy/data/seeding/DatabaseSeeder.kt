package com.example.telepathy.data.seeding

import android.util.Log
import com.example.telepathy.data.AppDatabase
import com.example.telepathy.data.entities.Message
import com.example.telepathy.data.entities.User
import com.example.telepathy.presentation.ui.theme.DarkLightBlue
import com.example.telepathy.presentation.ui.theme.DarkGreen
import com.example.telepathy.presentation.ui.theme.DarkTeal
import com.example.telepathy.presentation.ui.theme.DarkVividBlue

class DatabaseSeeder(
    private val database: AppDatabase
) {
    suspend fun seed() {

        Log.d("SEED", "Started seeding...")

        val userDao = database.userDao()
        val messageDao = database.messageDao()

        val users = listOf(
            User(id = 0, name = "Alice", description = "Loves painting", color = DarkLightBlue, deviceId = "Alice"),
            User(id = 0, name = "Bob", description = "Traveler", color = DarkGreen, deviceId = "Bob"),
            User(id = 0, name = "Charlie", description = "Gamer", color = DarkVividBlue, deviceId = "Charlie"),
            User(id = 0, name = "Diana", description = "Chef", color = DarkTeal, deviceId = "Diana")
        )
        userDao.insertUsers(users)

        val contacts = listOf(
            listOf(1, 2),
            listOf(1, 3),
            listOf(2, 3),
            listOf(2, 4),
            listOf(3, 4),
        )

        val messages = contacts.flatMap { contact ->
            val userId = contact[0]
            val contactId = contact[1]

            listOf(
                Message(
                    id = 0, // Auto-generowany
                    content = "Hi, how are you?",
                    senderId = userId,
                    recipientId = contactId,
                    timestamp = System.currentTimeMillis() - 60 * 60 * 1000
                ),
                Message(
                    id = 0, // Auto-generowany
                    content = "I'm good! How about you?",
                    senderId = contactId,
                    recipientId = userId,
                    timestamp = System.currentTimeMillis() - 30 * 60 * 1000
                )
            )
        }
        messageDao.insertMessages(messages)

        Log.d("SEED", "Finished seeding.")
    }

}