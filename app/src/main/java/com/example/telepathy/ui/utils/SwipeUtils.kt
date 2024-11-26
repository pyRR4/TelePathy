package com.example.telepathy.ui.utils

import android.util.Log
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


fun Modifier.swipeToNavigate(
    coroutineScope: CoroutineScope,
    onSwipeLeft: (() -> Unit)? = null,
    onSwipeRight: (() -> Unit)? = null,
    onSwipeUp: (() -> Unit)? = null,
    onSwipeDown: (() -> Unit)? = null,
    isSwipeHandled: MutableState<Boolean>,
    isNavigating: MutableState<Boolean>
): Modifier = this.pointerInput(Unit) {
    detectDragGestures { change, dragAmount ->
        change.consume()


        if (!isSwipeHandled.value && !isNavigating.value) {
            Log.d("SwipeGesture", "Drag Amount: x = ${dragAmount.x}, y = ${dragAmount.y}")
            when {
                dragAmount.x < -100f && onSwipeLeft != null -> {
                    Log.d("SwipeGesture", "Swipe Left detected")
                    isNavigating.value = true
                    coroutineScope.launch {
                        onSwipeLeft()
                        delay(200)
                        isSwipeHandled.value = true
                        isNavigating.value = false
                    }
                }
                dragAmount.y < -100f && onSwipeDown != null -> {
                    Log.d("SwipeGesture", "Swipe Down detected")
                    isNavigating.value = true
                    coroutineScope.launch {
                        onSwipeDown()
                        delay(200)
                        isSwipeHandled.value = true
                        isNavigating.value = false
                    }
                }
                dragAmount.x > 100f && onSwipeRight != null -> {
                    Log.d("SwipeGesture", "Swipe Right detected")
                    isNavigating.value = true
                    coroutineScope.launch {
                        onSwipeRight()
                        delay(200)
                        isSwipeHandled.value = true
                        isNavigating.value = false
                    }
                }
                dragAmount.y > 100f && onSwipeUp != null -> {
                    Log.d("SwipeGesture", "Swipe Up detected")
                    isNavigating.value = true
                    coroutineScope.launch {
                        onSwipeUp()
                        delay(200)
                        isSwipeHandled.value = true
                        isNavigating.value = false
                    }
                }
            }
        }
    }
}
