package com.example.telepathy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.telepathy.ui.utils.detectHorizontalSwipeGestures
import com.example.telepathy.ui.utils.detectVerticalSwipeGestures

@Composable
fun MainScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan)
            .detectVerticalSwipeGestures(
                onSwipeUp = { navController.navigate("settings") }
            )
            .detectHorizontalSwipeGestures(
                onSwipeRight = { navController.navigate("contacts") }
            )
    ) {
        Text(
            text = "Main Screen",
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.h4
        )
    }
}

@Composable
fun SettingsScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Green)
            .detectVerticalSwipeGestures(
                onSwipeDown = {
                    if (!navController.popBackStack("main", inclusive = false)) {
                        navController.navigate("main")  // Ensures navigation back to main
                    }
                }
            )
    ) {
        Text(
            text = "Settings Screen",
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.h4
        )
    }
}

@Composable
fun ContactsScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Magenta)
            .detectHorizontalSwipeGestures(
                onSwipeLeft = {
                    if (!navController.popBackStack("main", inclusive = false)) {
                        navController.navigate("main")  // Ensures navigation back to main
                    }
                }
            )
    ) {
        Text(
            text = "Contacts Screen",
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.h4
        )
    }
}
