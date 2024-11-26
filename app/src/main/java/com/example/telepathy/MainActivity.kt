package com.example.telepathy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.telepathy.ui.CustomButton
import com.example.telepathy.ui.screens.ButtonIcon
import com.example.telepathy.ui.screens.Contact
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
    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
        composable("contacts") { ContactsScreen(navController, sampleContacts()) }
    }
}

fun sampleContacts(): List<Contact> {
    return listOf(
        Contact(
            imageDrawable = R.drawable.test,
            name = "Remonty24H",
            isFromUser = false,
            message = "Kiedy mam przyjechać na robotę?",
            time = "15:37",
            backgroundColor = Color(0xFF8B0000)
        ),
        Contact(
            imageDrawable = R.drawable.test,
            name = "Łania23",
            isFromUser = false,
            message = "Sarna nie jest żoną jelenia !! :/",
            time = "04:04",
            backgroundColor = Color(0xFF696969)
        ),
        Contact(
            imageDrawable = R.drawable.test,
            name = "AmatorUczciwiec000",
            isFromUser = true,
            message = "Siema, co tam?",
            time = "12:37",
            backgroundColor = Color(0xFF4682B4)
        ),
        Contact(
            imageDrawable = R.drawable.test,
            name = "AmatorUczciwiec000",
            isFromUser = true,
            message = "Siema, co tam?",
            time = "12:37",
            backgroundColor = Color(0xFF4682B4)
        ),
        Contact(
            imageDrawable = R.drawable.test,
            name = "AmatorUczciwiec000",
            isFromUser = true,
            message = "Siema, co tam?",
            time = "12:37",
            backgroundColor = Color(0xFF4682B4)
        ),
        Contact(
            imageDrawable = R.drawable.test,
            name = "AmatorUczciwiec000",
            isFromUser = true,
            message = "Siema, co tam?",
            time = "12:37",
            backgroundColor = Color(0xFF4682B4)
        ),
        Contact(
            imageDrawable = R.drawable.test,
            name = "AmatorUczciwiec000",
            isFromUser = true,
            message = "Siema, co tam?",
            time = "12:37",
            backgroundColor = Color(0xFF4682B4)
        ),
        Contact(
            imageDrawable = R.drawable.test,
            name = "AmatorUczciwiec000",
            isFromUser = true,
            message = "Siema, co tam?",
            time = "12:37",
            backgroundColor = Color(0xFF4682B4)
        )
    )
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