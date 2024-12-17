package com.example.telepathy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.telepathy.presentation.ui.theme.TelePathyTheme
import com.example.telepathy.domain.users.UsersRepository
import com.example.telepathy.presentation.navigation.AnimatedNavHost
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.example.telepathy.data.*
import com.example.telepathy.presentation.ui.theme.UserColors


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
    TelePathyTheme {
        val context = LocalContext.current
        val userRepository = UsersRepository()

        LocalPreferences.loadLocalUser( // test lokal user ------------------------
            id = 0,
            name = "Lokal",
            color = Color(0xFFE81A1A),
            description = "Jestem Lokalny",
            context = context,
            avatarResId = R.drawable.test2
        )

        val currentScreen = remember { mutableStateOf("contacts") }
        val navController = rememberNavController()

        AnimatedNavHost(
            navController = navController,
            startDestination = "mainscreens",
            userRepository = userRepository,
            context = context,
            currentScreen = currentScreen
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TelePathyPreview() {
    TelePathyTheme {
        val currentScreen = remember { mutableStateOf("contacts") }
        val userRepository = UsersRepository()
        val navController = rememberNavController()
        val context = LocalContext.current
    }
} 