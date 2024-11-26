package com.example.telepathy.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.navigation.NavHostController
import com.example.telepathy.ui.theme.TelePathyTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainScreen(navController: NavHostController) {
    var isSwipeHandled by remember { mutableStateOf(false) }
    var isNavigating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Handle back press manually to prevent conflict
    TelePathyTheme {
        BackHandler {
            if (!isSwipeHandled && !isNavigating) {
                Log.d("BackPress", "MainScreen - Back Pressed")
                // Optionally, you can pop back or prevent it
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Cyan)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        // Log swipe gestures and the swipe amount
                        Log.d("SwipeGesture", "MainScreen - Swipe Amount (X, Y): (${dragAmount.x}, ${dragAmount.y})")

                        if (!isSwipeHandled && !isNavigating) {
                            // Vertical swipe detection
                            if (dragAmount.y < -100f) {  // Swipe up to go to Settings
                                Log.d("SwipeGesture", "MainScreen - Vertical Swipe: Up")
                                isNavigating = true
                                coroutineScope.launch {
                                    delay(200)  // Delay to ensure swipe completion
                                    navController.navigate("settings")
                                    isSwipeHandled = true  // Mark swipe as handled
                                    isNavigating = false
                                }
                            } else if (dragAmount.y > 100f) {  // Swipe down (Optional)
                                Log.d("SwipeGesture", "MainScreen - Vertical Swipe: Down")
                            }

                            // Horizontal swipe detection
                            if (dragAmount.x > 100f) {  // Swipe right to go to Contacts
                                Log.d("SwipeGesture", "MainScreen - Horizontal Swipe: Right")
                                isNavigating = true
                                coroutineScope.launch {
                                    delay(200)  // Delay to ensure swipe completion
                                    navController.navigate("contacts")
                                    isSwipeHandled = true  // Mark swipe as handled
                                    isNavigating = false
                                }
                            } else if (dragAmount.x < -100f) {  // Swipe left (Optional)
                                Log.d("SwipeGesture", "MainScreen - Horizontal Swipe: Left")
                            }
                        }
                    }
                }
        ) {
            Text(
                text = "Available Around Screen",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.headlineSmall
            )
        }
        }
}
