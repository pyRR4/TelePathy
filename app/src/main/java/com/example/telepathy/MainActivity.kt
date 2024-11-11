package com.example.telepathy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.telepathy.ui.screens.ContactCard
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
        composable("contacts") { ContactsScreen(navController) }
    }
}



@Preview(showBackground = true)
@Composable
fun TelePathyPreview() {
    TelePathyTheme {
        //ContactsScreen()
        ContactCard(
            R.drawable.test,
            "AmatorUczciwiec000",
            true,
            "siema",
            "15:37",
            Color.Blue,
            { println("Button clicked!") }
        )
    }
}