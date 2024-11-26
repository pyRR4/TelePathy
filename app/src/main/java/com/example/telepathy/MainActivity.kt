package com.example.telepathy

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.telepathy.clases.Message
import com.example.telepathy.clases.User
import com.example.telepathy.ui.CustomButton
import com.example.telepathy.ui.screens.ButtonIcon
import com.example.telepathy.ui.screens.MainScreen
import com.example.telepathy.ui.screens.SettingsScreen
import com.example.telepathy.ui.screens.ContactsScreen
import com.example.telepathy.ui.theme.TelePathyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable

fun MyApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
        composable("contacts") { ContactsScreen(navController, sampleUsers(context)) }
    }
}

fun sampleUsers(context: Context): List<User> {

    // Funkcja pomocnicza do ładowania Bitmap z drawable
    fun loadAvatar(resId: Int): Bitmap {
        return BitmapFactory.decodeResource(context.resources, resId)
    }

    val user1 = User(
        name = "AmatorUczciwiec000",
        color = Color(0xFF4682B4), // Kolor niebieski
        avatar = loadAvatar(R.drawable.test), // Załaduj avatar z drawable
        chatHistory = mutableListOf(
            Message(sender = "AmatorUczciwiec000", content = "Siema, co tam?", timestamp = System.currentTimeMillis(), fromLocalUser = true),
            Message(sender = "AmatorUczciwiec000", content = "Chciałem pogadać o czymś ważnym.", timestamp = System.currentTimeMillis(), fromLocalUser = true),
            Message(sender = "Remonty24H", content = "Cześć, wszystko w porządku?", timestamp = System.currentTimeMillis(), fromLocalUser = false),
            Message(sender = "AmatorUczciwiec000", content = "Tak, wszystko ok, a u Ciebie?", timestamp = System.currentTimeMillis(), fromLocalUser = true)
        ),
        isLocalUser = false
    )

    val user2 = User(
        name = "Remonty24H",
        color = Color(0xFF8B0000), // Kolor czerwony
        avatar = loadAvatar(R.drawable.test2), // Załaduj avatar z drawable
        chatHistory = mutableListOf(
            Message(sender = "Remonty24H", content = "Kiedy mam przyjechać na robotę?", timestamp = System.currentTimeMillis(), fromLocalUser = false),
            Message(sender = "AmatorUczciwiec000", content = "W środę o 10:00.", timestamp = System.currentTimeMillis(), fromLocalUser = true),
            Message(sender = "Remonty24H", content = "Ok, do zobaczenia!", timestamp = System.currentTimeMillis(), fromLocalUser = false)
        ),
        isLocalUser = false
    )

    val user3 = User(
        name = "Łania23",
        color = Color(0xFF696969), // Kolor szary
        avatar = loadAvatar(R.drawable.test3), // Załaduj avatar z drawable
        chatHistory = mutableListOf(
            Message(sender = "Łania23", content = "Sarna nie jest żoną jelenia!! :/", timestamp = System.currentTimeMillis(), fromLocalUser = false),
            Message(sender = "AmatorUczciwiec000", content = "Tak, wiem! To tylko żart :)", timestamp = System.currentTimeMillis(), fromLocalUser = true),
            Message(sender = "Łania23", content = "Haha, dobra! :D", timestamp = System.currentTimeMillis(), fromLocalUser = false)
        ),
        isLocalUser = false
    )

    // Dodaj kolejnych użytkowników
    val user4 = User(
        name = "TechSupport42",
        color = Color(0xFF32CD32), // Kolor zielony
        avatar = loadAvatar(R.drawable.test),
        chatHistory = mutableListOf(
            Message(sender = "TechSupport42", content = "W czym mogę pomóc?", timestamp = System.currentTimeMillis(), fromLocalUser = false),
            Message(sender = "AmatorUczciwiec000", content = "Mam problem z aplikacją, nie mogę się zalogować.", timestamp = System.currentTimeMillis(), fromLocalUser = true),
            Message(sender = "TechSupport42", content = "Proszę spróbować zresetować hasło.", timestamp = System.currentTimeMillis(), fromLocalUser = false)
        ),
        isLocalUser = false
    )

    val user5 = User(
        name = "PixelArtist",
        color = Color(0xFFFFA500), // Kolor pomarańczowy
        avatar = loadAvatar(R.drawable.test2),
        chatHistory = mutableListOf(
            Message(sender = "PixelArtist", content = "Ostatnio stworzyłem nowe pixel arty.", timestamp = System.currentTimeMillis(), fromLocalUser = false),
            Message(sender = "AmatorUczciwiec000", content = "Wow, fajnie! Pokażesz?", timestamp = System.currentTimeMillis(), fromLocalUser = true),
            Message(sender = "PixelArtist", content = "Jasne, zaraz wyślę zdjęcie.", timestamp = System.currentTimeMillis(), fromLocalUser = false)
        ),
        isLocalUser = false
    )

    return listOf(user1, user2, user3, user4, user5)
}

@Preview(showBackground = true)
@Composable
fun TelePathyPreview() {
    TelePathyTheme {
        CustomButton(
            name = "AmatorUczciwiec000",
            backgroundColor = Color(0xFF4682B4),
            image = { ButtonIcon(painterResource(R.drawable.test), Modifier) },
            onClick = { /* Handle click for this contact */ }
        )
      //  SettingsScreen()
//
//        val navController = rememberNavController()
//        ContactsScreen(navController, sampleContacts())
//        ContactCard(
//            imageDrawable = R.drawable.test,
//            name = "AmatorUczciwiec000",
//            isFromUser = true,
//            message = "Siema, co tam?",
//            time = "12:37",
//            backgroundColor = Color(0xFF4682B4)
//        ) { }
    }
}