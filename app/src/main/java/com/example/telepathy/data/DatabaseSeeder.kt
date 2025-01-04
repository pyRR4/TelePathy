package com.example.telepathy.data

import com.example.telepathy.data.entities.Contact
import com.example.telepathy.data.entities.Message
import com.example.telepathy.data.entities.User
import androidx.compose.ui.graphics.Color

class DatabaseSeeder(
    private val database: AppDatabase
) {
    suspend fun seed() {
        val userDao = database.userDao()
        val messageDao = database.messageDao()
        val contactDao = database.contactDao()

        // Przykładowe dane użytkowników
        val users = listOf(
            User(id = 0, name = "Alice", description = "Loves painting", color = Color.Blue),
            User(id = 0, name = "Bob", description = "Traveler", color = Color.Green),
            User(id = 0, name = "Charlie", description = "Gamer", color = Color.Red),
            User(id = 0, name = "Diana", description = "Chef", color = Color.Yellow)
        )
        userDao.insertUsers(users)

        // Przykładowe dane kontaktów
        val contacts = listOf(
            Contact(id = 0, userId = 1, contactId = 2),
            Contact(id = 0, userId = 1, contactId = 3),
            Contact(id = 0, userId = 2, contactId = 3),
            Contact(id = 0, userId = 2, contactId = 4),
            Contact(id = 0, userId = 3, contactId = 4)
        )
        contactDao.insertContacts(contacts)

        // Tworzenie wiadomości dla kontaktów
        val messages = contacts.flatMap { contact ->
            val userId = contact.userId
            val contactId = contact.contactId

            // Przykładowe wiadomości między użytkownikiem a kontaktem
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
    }

}