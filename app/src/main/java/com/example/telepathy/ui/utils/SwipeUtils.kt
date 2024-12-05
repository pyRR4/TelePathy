package com.example.telepathy.ui.utils

import android.content.Context
import android.util.Log
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.with
import androidx.compose.runtime.*
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.telepathy.ui.screens.AvailableAroundScreen
import com.example.telepathy.ui.screens.ContactsScreen
import com.example.telepathy.ui.screens.MainScreen
import com.example.telepathy.ui.screens.SettingsScreen
import com.example.telepathy.ui.screens.TalkScreen
import com.example.telepathy.ui.users.UsersRepository


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


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedNavHost(
    navController: NavHostController,
    startDestination: String,
    userRepository: UsersRepository,
    context: Context,
    currentScreen: MutableState<String>
) {
    androidx.navigation.compose.NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(
            route = "mainscreens",
            enterTransition = {
                slideInHorizontally(initialOffsetX = { -it }) + fadeIn()
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { it }) + fadeIn()
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
            }
        ) {
            MainScreen(navController, userRepository, context, currentScreen)
        }

        composable(
            route = "talkscreen/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType }),
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }) + fadeIn()
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
            }
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId")
            val user = userId?.let { userRepository.getUserById(context, it) }
            user?.let { TalkScreen(navController, it) }
        }
    }
}
