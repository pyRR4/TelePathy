package com.example.telepathy.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.telepathy.screens.Ekran1
import com.example.telepathy.screens.Ekran2

@Composable
fun NavGraph() {
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
