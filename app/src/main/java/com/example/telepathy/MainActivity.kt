package com.example.telepathy

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.telepathy.presentation.ui.theme.TelePathyTheme
import com.example.telepathy.presentation.navigation.AnimatedNavHost
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.telepathy.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
        val database = AppDatabase.getDatabase(applicationContext)
        CoroutineScope(Dispatchers.IO).launch {
            val users = database.userDao().getAllUsers()
            Log.d("Seed", "Loaded users: $users")
        }
    }
}

@Composable
fun MyApp() {
    TelePathyTheme {
        val context = LocalContext.current

        LocalPreferences.loadLocalUser( // test lokal user ------------------------
            id = 0,
            name = "Lokal",
            color = Color(0xFFE57373),
            description = "Jestem Lokalny",
            context = context,
            avatarResId = R.drawable.test2
        )

        val currentScreen = remember { mutableStateOf("contacts") }
        val navController = rememberNavController()
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.DarkGray
        ) {
            AnimatedNavHost(
                navController = navController,
                startDestination = "contactsscreen",
                context = context,
                localUserId = 1,
                currentScreen = currentScreen
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TelePathyPreview() {
    TelePathyTheme {
    }
} 