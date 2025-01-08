package com.example.telepathy.data.seeding;

import android.content.Context
import android.util.Log
import com.example.telepathy.data.AppDatabase;
import com.example.telepathy.data.PreferencesManager
import com.example.telepathy.data.entities.User
import com.example.telepathy.presentation.ui.theme.DarkUserColors
import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.FingerprinterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

class DefaultUserSeeder(
    private val database: AppDatabase,
    private val context: Context
) {
    fun seed() {
        val preferencesManager = PreferencesManager(context)
        val fingerprinter = FingerprinterFactory.create(context)
        logSharedPreferences(context)

        CoroutineScope(Dispatchers.IO).launch {

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
                        color = DarkUserColors.random(),
                        avatar = null,
                        deviceId = deviceId
                    )
                    database.userDao().insert(defaultUser)
                    Log.d("Seed", "Created default user: $defaultUser")
                } else {
                    Log.d("Seed", "Database already contains users.")
                }
            }

            logSharedPreferences(context)
            logAllUsers(database)
        }
    }

    private fun logSharedPreferences(context: Context) {
        val sharedPreferences = context.getSharedPreferences("telepathy_prefs", Context.MODE_PRIVATE)
        val allEntries = sharedPreferences.all
        Log.d("SharedPreferences", "Zawartość SharedPreferences:")
        for ((key, value) in allEntries) {
            Log.d("SharedPreferences", "Key: $key, Value: $value")
        }
    }

    private suspend fun logAllUsers(database: AppDatabase) {
        val allUsers = database.userDao().getAllUsers().first()
        Log.d("Database", "Zawartość bazy danych (Użytkownicy):")
        for (user in allUsers) {
            Log.d("Database", "User: $user")
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun fetchDeviceId(fingerprinter: Fingerprinter): String = suspendCancellableCoroutine { continuation ->
        fingerprinter.getDeviceId(Fingerprinter.Version.V_5) { result ->
            if (continuation.isActive) {
                continuation.resume(result.deviceId) {

                }
            }
        }
    }
}
