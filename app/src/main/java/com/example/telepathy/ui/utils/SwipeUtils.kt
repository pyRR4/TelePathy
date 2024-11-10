package com.example.telepathy.ui.utils

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.detectVerticalSwipeGestures(
    onSwipeUp: (() -> Unit)? = null,
    onSwipeDown: (() -> Unit)? = null,
    swipeThreshold: Dp = 50.dp
): Modifier = pointerInput(Unit) {
    val thresholdPx = swipeThreshold.toPx()
    detectDragGestures { change, dragAmount ->
        change.consume()
        if (dragAmount.y > thresholdPx) onSwipeDown?.invoke()
        else if (dragAmount.y < -thresholdPx) onSwipeUp?.invoke()
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
