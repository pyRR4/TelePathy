package com.example.telepathy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.telepathy.screens.Ekran1  // Import poprawnego pakietu
import com.example.telepathy.screens.Ekran2  // Import poprawnego pakietu
import com.example.telepathy.ui.theme.TelePathyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TelePathyTheme {
                Surface(modifier = Modifier.fillMaxSize()) {//weewe
                    AppNavGraph()
                }
            }
        }
    }
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "ekran1") {
        composable("ekran1") {
            Ekran1(onNavigateToEkran2 = { navController.navigate("ekran2") })
        }
        composable("ekran2") {
            Ekran2(onNavigateToEkran1 = { navController.navigate("ekran1") })
        }
    }
}
