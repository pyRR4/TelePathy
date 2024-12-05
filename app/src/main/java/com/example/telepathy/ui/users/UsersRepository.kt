package com.example.telepathy.ui.users

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.Color
import com.example.telepathy.clases.Message
import com.example.telepathy.clases.User
import com.example.telepathy.R

class UsersRepository {

    private var userList: List<User>? = null


    private fun sampleUsers(context: Context): List<User> {

        fun loadAvatar(resId: Int): Bitmap {
            return BitmapFactory.decodeResource(context.resources, resId)
        }

        val currentTime = System.currentTimeMillis()

        val user1 = User(
            id = 1,
            name = "AmatorUczciwiec000",
            color = Color(0xFF4682B4),
            avatar = loadAvatar(R.drawable.test1),
            chatHistory = mutableListOf(
                Message(sender = "AmatorUczciwiec000", content = "Siema, co tam?", timestamp = currentTime - 10000, fromLocalUser = true),  // 10 sek temu
                Message(sender = "AmatorUczciwiec000", content = "Chciałem pogadać o czymś ważnym.", timestamp = currentTime - 30000, fromLocalUser = true), // 30 sek temu
                Message(sender = "Remonty24H", content = "Cześć, wszystko w porządku?", timestamp = currentTime - 60000, fromLocalUser = false), // 1 minuta temu
                Message(sender = "AmatorUczciwiec000", content = "Tak, wszystko ok, a u Ciebie?", timestamp = currentTime - 90000, fromLocalUser = true) // 1,5 minuty temu
            ),
            isLocalUser = false,
            description = "Uwielbiam grać w gry i pomagać innym!" // Opis użytkownika
        )

        val user2 = User(
            id = 2,
            name = "Remonty24H",
            color = Color(0xFF8B0000), // Kolor czerwony
            avatar = loadAvatar(R.drawable.test2), // Załaduj avatar z drawable
            chatHistory = mutableListOf(
                Message(sender = "Remonty24H", content = "Kiedy mam przyjechać na robotę?", timestamp = currentTime - 180000, fromLocalUser = false), // 3 minuty temu
                Message(sender = "AmatorUczciwiec000", content = "W środę o 10:00.", timestamp = currentTime - 360000, fromLocalUser = true), // 6 minut temu
                Message(sender = "Remonty24H", content = "Ok, do zobaczenia!", timestamp = currentTime - 540000, fromLocalUser = false) // 9 minut temu
            ),
            isLocalUser = false,
            description = "Specjalista od remontów. Zawsze punktualny!" // Opis użytkownika
        )

        val user3 = User(
            id = 3,
            name = "Łania23",
            color = Color(0xFF696969), // Kolor szary
            avatar = loadAvatar(R.drawable.test3), // Załaduj avatar z drawable
            chatHistory = mutableListOf(
                Message(sender = "Łania23", content = "Sarna nie jest żoną jelenia!! :/", timestamp = currentTime - 120000, fromLocalUser = false), // 2 minuty temu
                Message(sender = "AmatorUczciwiec000", content = "Tak, wiem! To tylko żart :)", timestamp = currentTime - 150000, fromLocalUser = true), // 2,5 minuty temu
                Message(sender = "Łania23", content = "Haha, dobra! :D", timestamp = currentTime - 180000, fromLocalUser = false) // 3 minuty temu
            ),
            isLocalUser = false,
            description = "Zafascynowana przyrodą i zwierzętami!" // Opis użytkownika
        )

        val user4 = User(
            id = 4,
            name = "TechSupport42",
            color = Color(0xFF32CD32), // Kolor zielony
            avatar = null,
            chatHistory = mutableListOf(
                Message(sender = "TechSupport42", content = "W czym mogę pomóc?", timestamp = currentTime - 720000, fromLocalUser = false), // 12 minut temu
                Message(sender = "AmatorUczciwiec000", content = "Mam problem z aplikacją, nie mogę się zalogować.", timestamp = currentTime - 900000, fromLocalUser = true), // 15 minut temu
                Message(sender = "TechSupport42", content = "Proszę spróbować zresetować hasło.", timestamp = currentTime - 1080000, fromLocalUser = false) // 18 minut temu
            ),
            isLocalUser = false,
            description = "Pomagam rozwiązywać problemy z technologią!" // Opis użytkownika
        )

        val user5 = User(
            id = 5,
            name = "PixelArtist",
            color = Color(0xFFFFA500), // Kolor pomarańczowy
            avatar = loadAvatar(R.drawable.test2),
            chatHistory = mutableListOf(
                Message(sender = "PixelArtist", content = "Ostatnio stworzyłem nowe pixel arty.", timestamp = currentTime - 240000, fromLocalUser = false), // 4 minuty temu
                Message(sender = "AmatorUczciwiec000", content = "Wow, fajnie! Pokażesz?", timestamp = currentTime - 300000, fromLocalUser = true), // 5 minut temu
                Message(sender = "PixelArtist", content = "Jasne, zaraz wyślę zdjęcie.", timestamp = currentTime - 360000, fromLocalUser = false) // 6 minut temu
            ),
            isLocalUser = false,
            description = "Tworzę sztukę w stylu pixel art!" // Opis użytkownika
        )

        return listOf(user1, user2, user3, user4, user5)
    }


    fun getUsers(context: Context): List<User> {
        if (userList == null) {
            userList = sampleUsers(context)
        }
        return userList ?: emptyList()
    }

    fun getUserById(context: Context, userId: Int): User? {
        return getUsers(context).find { it.id == userId }
    }
}
