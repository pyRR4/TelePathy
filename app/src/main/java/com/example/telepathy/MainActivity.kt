package com.example.telepathy

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.telepathy.presentation.ui.theme.TelePathyTheme
import com.example.telepathy.presentation.navigation.AnimatedNavHost
import com.example.telepathy.data.*
import com.example.telepathy.data.entities.User
import com.example.telepathy.presentation.ui.theme.UserColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = applicationContext
        val preferencesManager = PreferencesManager(context)
        val database = AppDatabase.getDatabase(context)


        CoroutineScope(Dispatchers.IO).launch {
            if (preferencesManager.isFirstLaunch()) {
                // Pierwsze uruchomienie aplikacji
                preferencesManager.setFirstLaunch(false)
                preferencesManager.savePin(null)
                preferencesManager.saveLocalUserId(0)

                val defaultUser = User(
                    id = 0,
                    name = "Default User",
                    description = "This is the default user",
                    color = UserColors.random(),
                    avatar = null
                )
                database.userDao().insert(defaultUser)
                Log.d("Seed", "Created default user: $defaultUser")
            }
        }

        setContent {
            MyApp()
        }
    }
}


@Composable
fun MyApp() {
    TelePathyTheme {
        val context = LocalContext.current

        val currentScreen = remember { mutableStateOf("contacts") }
        val navController = rememberNavController()

        AnimatedNavHost(
            navController = navController,
            startDestination = "mainscreens",
            context = context,
            localUserId = 2,
            currentScreen = currentScreen
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TelePathyPreview() {
    TelePathyTheme {
    }
} 