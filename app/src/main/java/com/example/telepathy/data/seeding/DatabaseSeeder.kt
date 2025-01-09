package com.example.telepathy.data.seeding

import android.content.Context
import android.util.Log
import com.example.telepathy.data.AppDatabase
import com.example.telepathy.data.PreferencesManager
import com.example.telepathy.data.entities.Message
import com.example.telepathy.data.entities.User
import com.example.telepathy.presentation.ui.theme.DarkLightBlue
import com.example.telepathy.presentation.ui.theme.DarkGreen
import com.example.telepathy.presentation.ui.theme.DarkTeal
import com.example.telepathy.presentation.ui.theme.DarkVividBlue
import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.FingerprinterFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.suspendCancellableCoroutine

class DatabaseSeeder(
    private val database: AppDatabase,
    private val context: Context
) {

    suspend fun seed() {
        seedDefaultUser()
        seedUsersAndMessages()
    }

    private suspend fun seedDefaultUser() {
        val preferencesManager = PreferencesManager(context)
        val fingerprinter = FingerprinterFactory.create(context)
        logSharedPreferences(context)

        val deviceId = fetchDeviceId(fingerprinter)

        if (preferencesManager.isFirstLaunch()) {
            preferencesManager.setFirstLaunch(false)
            preferencesManager.savePin(null)

            val usersCount = database.userDao().getAllUsers().first().size
            if (usersCount == 0) {
                preferencesManager.saveLocalUserDeviceId(deviceId)

                val defaultUser = User(
                    id = 0,
                    name = "Default User",
                    description = "This is the default user",
                    color = DarkLightBlue, // Replace with any default color
                    avatar = null,
                    deviceId = deviceId
                )
                database.userDao().insert(defaultUser)
                val localUser = database.userDao().getUserByDeviceId(deviceId).first()
                preferencesManager.saveLocalUserId(localUser.id)

                Log.d("DatabaseSeeder", "Created default user: $defaultUser")
            } else {
                Log.d("DatabaseSeeder", "Database already contains users.")
            }
        }

        logSharedPreferences(context)
        logAllUsers()
    }

    private suspend fun seedUsersAndMessages() {
        Log.d("DatabaseSeeder", "Started seeding users and messages...")

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
                    id = 0,
                    content = "Hi, how are you?",
                    senderId = userId,
                    recipientId = contactId,
                    timestamp = System.currentTimeMillis() - 60 * 60 * 1000
                ),
                Message(
                    id = 0,
                    content = "I'm good! How about you?",
                    senderId = contactId,
                    recipientId = userId,
                    timestamp = System.currentTimeMillis() - 30 * 60 * 1000
                )
            )
        }
        messageDao.insertMessages(messages)

        Log.d("DatabaseSeeder", "Finished seeding users and messages.")
    }

    private fun logSharedPreferences(context: Context) {
        val sharedPreferences = context.getSharedPreferences("telepathy_prefs", Context.MODE_PRIVATE)
        val allEntries = sharedPreferences.all
        Log.d("SharedPreferences", "Zawartość SharedPreferences:")
        for ((key, value) in allEntries) {
            Log.d("SharedPreferences", "Key: $key, Value: $value")
        }
    }

    private suspend fun logAllUsers() {
        val allUsers = database.userDao().getAllUsers().first()
        Log.d("Database", "Zawartość bazy danych (Użytkownicy):")
        for (user in allUsers) {
            Log.d("Database", "User: $user")
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun fetchDeviceId(fingerprinter: Fingerprinter): String = suspendCancellableCoroutine { continuation ->
        fingerprinter.getDeviceId(Fingerprinter.Version.V_5) { result ->
            if (continuation.isActive) {
                continuation.resume(result.deviceId) {}
            }
        }
    }
}
