package com.example.telepathy

import com.example.telepathy.data.entities.User
import com.example.telepathy.domain.serialization.deserializeUser
import com.example.telepathy.domain.serialization.isCompleteJson
import com.example.telepathy.domain.serialization.serializeUser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UserSerializationTest {

    @Test
    fun `serializeUser should correctly serialize a User object`() {
        val user = User(
            id = 1,
            name = "John Doe",
            description = "Test user",
            color = 123456,
            avatar = null,
            deviceId = "device123"
        )

        val json = serializeUser(user)
        assertTrue(json.contains("\"name\":\"John Doe\""))
        assertTrue(json.contains("\"deviceId\":\"device123\""))
    }

    @Test
    fun `deserializeUser should correctly deserialize a User JSON string`() {
        val json = """
            {
                "id":1,
                "name":"John Doe",
                "description":"Test user",
                "color":123456,
                "avatar":null,
                "deviceId":"device123"
            }
        """
        val user = deserializeUser(json)
        assertEquals("John Doe", user.name)
        assertEquals("device123", user.deviceId)
    }

    @Test
    fun `isCompleteJson should return true for valid JSON`() {
        val json = """{"id":1,"name":"John"}"""
        assertTrue(isCompleteJson(json))
    }

    @Test
    fun `isCompleteJson should return false for invalid JSON`() {
        val json = """{"id":1,"name":"John"""
        assertTrue(!isCompleteJson(json))
    }
}
