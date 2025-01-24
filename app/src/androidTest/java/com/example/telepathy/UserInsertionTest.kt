package com.example.telepathy

import com.example.telepathy.data.entities.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class UserInsertionTest : BaseTestSetup() {

    @Test
    fun shouldInsertAndRetrieveTestUsers() = runBlocking {

        val usersInDb = database.userDao().getAllUsers().first()

        val firstUser = usersInDb[0]
        assertEquals(1, firstUser.id)
        assertEquals("Test User", firstUser.name)
        assertEquals("testDeviceId", firstUser.deviceId)

    }

}
