package com.example.telepathy.domain.serialization

import com.example.telepathy.data.entities.User
import com.google.gson.Gson

val gson = Gson()

fun serializeUser(user: User): String {
    return gson.toJson(user)
}

fun deserializeUser(json: String): User {
    return gson.fromJson(json, User::class.java)
}
