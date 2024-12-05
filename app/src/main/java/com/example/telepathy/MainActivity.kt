package com.example.telepathy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.telepathy.ui.theme.TelePathyTheme
import com.example.telepathy.ui.users.UsersRepository
import com.example.telepathy.ui.utils.AnimatedNavHost


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