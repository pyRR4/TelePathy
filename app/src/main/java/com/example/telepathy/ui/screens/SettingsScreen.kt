package com.example.telepathy.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.telepathy.R

@Composable
fun SettingsScreen(navController: NavHostController) {
    var isSwipeHandled by remember { mutableStateOf(false) }
    var isNavigating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Handle back press manually
    BackHandler {
        if (!isSwipeHandled && !isNavigating) {
            Log.d("BackPress", "SettingsScreen - Back Pressed")
            coroutineScope.launch {
                delay(200)  // Delay to ensure swipe completion
                navController.popBackStack()  // Go back to Main Screen
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    // Log swipe gestures and the swipe amount
                    Log.d("SwipeGesture", "SettingsScreen - Swipe Amount (X, Y): (${dragAmount.x}, ${dragAmount.y})")

                    if (!isSwipeHandled && !isNavigating) {
                        // Vertical swipe detection
                        if (dragAmount.y > 100f) {  // Swipe down to go back to Main Screen
                            Log.d("SwipeGesture", "SettingsScreen - Vertical Swipe: Down")
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
            text = "Settings Screen",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


@Composable
fun CustomButton(
    name: String,
    backgroundColor: Color,
    image: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            //.fillMaxWidth(0.8f)
            .width(570.dp)
            .height(100.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        val buttonModifier = Modifier
            .size(height = 140.dp, width = 570.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(20.dp))
            .padding(16.dp)

        Row(
            modifier = buttonModifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(painterResource(image), modifier = Modifier)

            Text(
                text = name,
                color = if (backgroundColor == Color.Black) Color.White else Color.Black,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}