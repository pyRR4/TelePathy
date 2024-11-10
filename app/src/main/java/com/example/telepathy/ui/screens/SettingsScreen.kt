package com.example.telepathy.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.navigation.NavHostController

@Composable
fun SettingsScreen(navController: NavHostController) {
    var isSwipeHandled by remember { mutableStateOf(false) }

    // Handle back press manually
    BackHandler {
        navController.popBackStack()  // Go back to Main Screen
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Green)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    // Log swipe gestures and the swipe amount
                    Log.d("SwipeGesture", "SettingsScreen - Swipe Amount (X, Y): (${dragAmount.x}, ${dragAmount.y})")

                    if (!isSwipeHandled) {
                        // Vertical swipe detection
                        if (dragAmount.y > 100f) {  // Swipe down to go back to Main Screen
                            Log.d("SwipeGesture", "SettingsScreen - Vertical Swipe: Down")
                            navController.popBackStack()
                            isSwipeHandled = true  // Mark swipe as handled
                        }
                    }
                }
            }
    ) {
        Text(
            text = "Settings Screen",
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.h4
        )
    }
}