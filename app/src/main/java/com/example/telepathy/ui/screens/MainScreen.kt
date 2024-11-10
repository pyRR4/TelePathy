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
fun MainScreen(navController: NavHostController) {
    // Handle back press manually
    BackHandler {
        // Prevent going back when on Main Screen (if needed, otherwise popBackStack can be used)
    }

    var isSwipeHandled by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    // Log swipe gestures and the swipe amount
                    Log.d("SwipeGesture", "MainScreen - Swipe Amount (X, Y): (${dragAmount.x}, ${dragAmount.y})")

                    if (!isSwipeHandled) {
                        // Vertical swipe detection
                        if (dragAmount.y < -100f) {  // Swipe up to go to Settings
                            Log.d("SwipeGesture", "MainScreen - Vertical Swipe: Up")
                            navController.navigate("settings")
                            isSwipeHandled = true  // Mark swipe as handled
                        } else if (dragAmount.y > 100f) {  // Swipe down (Optional)
                            Log.d("SwipeGesture", "MainScreen - Vertical Swipe: Down")
                        }

                        // Horizontal swipe detection
                        if (dragAmount.x > 100f) {  // Swipe right to go to Contacts
                            Log.d("SwipeGesture", "MainScreen - Horizontal Swipe: Right")
                            navController.navigate("contacts")
                            isSwipeHandled = true  // Mark swipe as handled
                        } else if (dragAmount.x < -100f) {  // Swipe left (Optional)
                            Log.d("SwipeGesture", "MainScreen - Horizontal Swipe: Left")
                        }
                    }
                }
            }
    ) {
        Text(
            text = "Main Screen",
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.h4
        )
    }
}