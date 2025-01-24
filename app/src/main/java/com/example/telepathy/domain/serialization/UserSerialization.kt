package com.example.telepathy.domain.serialization

import com.example.telepathy.data.entities.User
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

val gson = Gson()

fun serializeUser(user: User): String {
    return gson.toJson(user)
}

fun deserializeUser(json: String): User {
    return gson.fromJson(json, User::class.java)
}

fun isCompleteJson(json: String): Boolean {
    return try {
        val gson = Gson()
        gson.fromJson(json, Any::class.java)
        true
    } catch (e: JsonSyntaxException) {
        false
    }
}