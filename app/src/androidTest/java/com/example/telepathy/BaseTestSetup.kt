package com.example.telepathy

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.telepathy.data.AppDatabase
import com.example.telepathy.data.PreferencesManager
import com.example.telepathy.data.entities.User
import com.example.telepathy.data.repositories.UserRepositoryImpl
import com.example.telepathy.presentation.viewmodels.SharedViewModel
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before

open class BaseTestSetup {

    lateinit var database: AppDatabase
    lateinit var preferencesManager: PreferencesManager

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        val userRepository = UserRepositoryImpl(database.userDao())
        val preferencesManager = PreferencesManager(context)

        runBlocking {
            insertTestUser(userRepository,preferencesManager)
        }
    }

    @After
    fun tearDown() {
        database.close()
    }

    private suspend fun insertTestUser(userRepository: UserRepositoryImpl, preferencesManager:PreferencesManager ) {
        val testUser = User(
            id = 0,
            name = "Test User",
            description = "This is a test user",
            color = 123456,
            avatar = null,
            deviceId = "testDeviceId"
        )
        userRepository.insert(testUser)
        preferencesManager.saveLocalUserId(1)
        preferencesManager.saveLocalUserDeviceId("testDeviceId")
    }
}
