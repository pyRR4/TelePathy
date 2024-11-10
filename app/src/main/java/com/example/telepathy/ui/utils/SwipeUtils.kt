package com.example.telepathy.ui.utils

import android.util.Log
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


fun Modifier.detectVerticalSwipeGestures(
    onSwipeUp: (() -> Unit)? = null,
    onSwipeDown: (() -> Unit)? = null,
    swipeThreshold: Dp = 30.dp  // Lower threshold for easier detection
): Modifier = pointerInput(Unit) {
    val thresholdPx = swipeThreshold.toPx()

    detectDragGestures { change, dragAmount ->
        change.consume() // Consume the gesture to prevent propagation

        if (dragAmount.y > thresholdPx) {
            Log.d("SwipeGesture", "Detected Swipe Down")  // Log swipe down detection
            onSwipeDown?.invoke()  // Trigger onSwipeDown if drag down exceeds threshold
        } else if (dragAmount.y < -thresholdPx) {
            Log.d("SwipeGesture", "Detected Swipe Up")    // Log swipe up detection
            onSwipeUp?.invoke()    // Trigger onSwipeUp if drag up exceeds threshold
        } else {
            Log.d("SwipeGesture", "Insufficient vertical drag")  // Log insufficient drag
        }
    }
}

fun Modifier.detectHorizontalSwipeGestures(
    onSwipeLeft: (() -> Unit)? = null,
    onSwipeRight: (() -> Unit)? = null,
    swipeThreshold: Dp = 50.dp
): Modifier = pointerInput(Unit) {
    val thresholdPx = swipeThreshold.toPx()
    detectDragGestures { change, dragAmount ->
        change.consume()
        if (dragAmount.x > thresholdPx) onSwipeRight?.invoke()
        else if (dragAmount.x < -thresholdPx) onSwipeLeft?.invoke()
    }
}
