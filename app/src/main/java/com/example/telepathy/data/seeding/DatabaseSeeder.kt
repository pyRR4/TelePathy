package com.example.telepathy.data.seeding

import android.util.Log
import com.example.telepathy.data.AppDatabase
import com.example.telepathy.data.entities.Contact
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
        val contactDao = database.contactDao()

        val users = listOf(
            User(id = 0, name = "Alice", description = "Loves painting", color = DarkLightBlue, deviceId = "Alice"),
            User(id = 0, name = "Bob", description = "Traveler", color = DarkGreen, deviceId = "Bob"),
            User(id = 0, name = "Charlie", description = "Gamer", color = DarkVividBlue, deviceId = "Charlie"),
            User(id = 0, name = "Diana", description = "Chef", color = DarkTeal, deviceId = "Diana")
        )
        userDao.insertUsers(users)

        val contacts = listOf(
            Contact(id = 0, userId = 1, contactId = 2),
            Contact(id = 0, userId = 1, contactId = 3),
            Contact(id = 0, userId = 2, contactId = 3),
            Contact(id = 0, userId = 2, contactId = 4),
            Contact(id = 0, userId = 3, contactId = 4)
        )
        contactDao.insertContacts(contacts)

        val messages = contacts.flatMap { contact ->
            val userId = contact.userId
            val contactId = contact.contactId

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