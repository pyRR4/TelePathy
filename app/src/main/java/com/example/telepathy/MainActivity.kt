package com.example.telepathy

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.telepathy.data.AppDatabase
import com.example.telepathy.data.PreferencesManager
import com.example.telepathy.domain.PermissionManager
import com.example.telepathy.presentation.navigation.AnimatedNavHost
import com.example.telepathy.presentation.ui.screens.LoadingScreen
import com.example.telepathy.presentation.ui.theme.TelePathyTheme
import com.example.telepathy.presentation.viewmodels.LoadingViewModel

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = remember { LoadingViewModel(this) }
            MyApp(viewModel)
        }
    }
}

@Composable
fun MyApp(viewModel: LoadingViewModel) {
    val isInitialized = viewModel.isInitialized.collectAsState()
    val navController = rememberNavController()
    val currentScreen = remember { mutableStateOf("contacts") }

    LaunchedEffect(isInitialized.value) {
        if (isInitialized.value) {
            navController.navigate("enter_pin_login") {
                popUpTo("loading") { inclusive = true }
            }
        }
    }

    TelePathyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Log.d("INITIALIZATION", "INIT STATE: $isInitialized")
            AnimatedNavHost(
                navController = navController,
                startDestination = "loading",
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
