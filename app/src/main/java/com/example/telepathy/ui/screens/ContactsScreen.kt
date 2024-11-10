package com.example.telepathy.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ContactsScreen(navController: NavHostController) {
    var isSwipeHandled by remember { mutableStateOf(false) }
    var isNavigating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Handle back press manually
    BackHandler {
        if (!isSwipeHandled && !isNavigating) {
            Log.d("BackPress", "ContactsScreen - Back Pressed")
            coroutineScope.launch {
                delay(200)  // Delay to ensure swipe completion
                navController.popBackStack()  // Go back to Main Screen
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Magenta)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    // Log swipe gestures and the swipe amount
                    Log.d("SwipeGesture", "ContactsScreen - Swipe Amount (X, Y): (${dragAmount.x}, ${dragAmount.y})")

                    if (!isSwipeHandled && !isNavigating) {
                        // Horizontal swipe detection
                        if (dragAmount.x < -100f) {  // Swipe left to go back
                            Log.d("SwipeGesture", "ContactsScreen - Horizontal Swipe: Left")
                            isNavigating = true
                            coroutineScope.launch {
                                delay(200)  // Delay to ensure swipe completion
                                navController.popBackStack()
                                isSwipeHandled = true  // Mark swipe as handled
                                isNavigating = false
                            }
                        }
                    }
                }
            }
    ) {
        Text(
            text = "Contacts Screen",
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.h4
        )
    }
}
