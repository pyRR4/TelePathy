package com.example.telepathy.presentation.navigation

import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.*
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.telepathy.presentation.ui.screens.MainScreen
import com.example.telepathy.presentation.ui.screens.TalkScreen
import com.example.telepathy.presentation.ui.screens.ConfirmPinScreen
import com.example.telepathy.presentation.ui.screens.EditProfileScreen
import com.example.telepathy.presentation.ui.screens.EnterNewPinScreen
import com.example.telepathy.presentation.ui.screens.EnterPinScreen
import kotlin.math.abs


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedNavHost(
    navController: NavHostController,
    startDestination: String,
    context: Context,
    currentScreen: MutableState<String>,
    localUserId: Int
) {
    val currentBackSetEntry: NavBackStackEntry? by navController.currentBackStackEntryAsState()

    androidx.navigation.compose.NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {

        composable(
            route = "mainscreens",
            enterTransition = {
                if(initialState.destination.route?.startsWith("talkscreen/") ?: false) {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = LinearEasing
                        )
                    )
                } else {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Up,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = LinearEasing
                        )
                    )
                }
            },
            exitTransition = {
                if(currentBackSetEntry?.destination?.route?.startsWith("talkscreen/") ?: false) {
                    fadeOut(
                        animationSpec = tween(durationMillis = 500)
                    )
                } else {
                    fadeOut(
                        animationSpec = tween(durationMillis = 500)
                    )
                }
            }
        ) {
            MainScreen(navController, context, localUserId, currentScreen)
        }

        composable(
            route = "talkscreen/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType }),
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(
                        durationMillis = 250,
                        easing = LinearEasing // interpolator
                    )
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(durationMillis = 500)
                )
            },
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable
            TalkScreen(
                navController = navController,
                localUserId = localUserId,
                remoteUserId = userId
            )
        }

        composable(
            route = "edit_profile",
            enterTransition = {
                slideInVertically(initialOffsetY = { it })
            },
            exitTransition = {
                slideOutVertically(targetOffsetY = { it })
            }
        ) {
            EditProfileScreen(navController)
        }

        //---------------------- Koniec animacji sprawdzonych ------------------------//

        composable(
            route = "enter_pin_login", // do sprawdzenia
            enterTransition = {
                slideInVertically(initialOffsetY = { it })
            },
            exitTransition = {
                slideOutVertically(targetOffsetY = { it })
            }
        ) {
            MainScreen(navController, context, localUserId, currentScreen)
        }

        composable(
            route = "enter_pin_settings", // pin przy probie zmiany pinu
            enterTransition = {
                slideInVertically(initialOffsetY = { it })
            },
            exitTransition = {
                slideOutVertically(targetOffsetY = { it })
            }
        ) {
            EnterPinScreen(navController, "enter_new_pin", onCancel = { navController.popBackStack() })
        }

        composable(
            route = "enter_new_pin",
            enterTransition = {
                slideInVertically(initialOffsetY = { it })
            },
            exitTransition = {
                slideOutVertically(targetOffsetY = { it })
            }
        ) {
            EnterNewPinScreen(
                navController = navController,
                onCancel = { navController.popBackStack() } // Navigate back on cancel
            )
        }

        composable(
            route = "confirm_new_pin/{pin}",
            arguments = listOf(navArgument("pin") { type = NavType.StringType }),
            enterTransition = {
                slideInVertically(initialOffsetY = { it })
            },
            exitTransition = {
                slideOutVertically(targetOffsetY = { it })
            }
        ) { backStackEntry ->
            val pinTemp = backStackEntry.arguments?.getString("pin") ?: ""
            ConfirmPinScreen(
                navController = navController,
                onCancel = { navController.popBackStack() }, // Navigate back on cancel
                pinTemp = pinTemp
            )
        }

    }
    }



fun Modifier.swipeToNavigate(
    coroutineScope: CoroutineScope,
    onSwipeLeft: (() -> Unit)? = null,
    onSwipeRight: (() -> Unit)? = null,
    onSwipeUp: (() -> Unit)? = null,
    onSwipeDown: (() -> Unit)? = null,
    isSwipeHandled: MutableState<Boolean>,
    isNavigating: MutableState<Boolean>,
    horizontalThreshold: Float = 30f,
    verticalThreshold: Float = 30f,
): Modifier = this.pointerInput(Unit) {
    detectDragGestures { change, dragAmount ->
        change.consume()

        if (!isSwipeHandled.value && !isNavigating.value) {
            Log.d("SwipeGesture", "Drag Amount: x = ${dragAmount.x}, y = ${dragAmount.y}")
            when {
                abs(dragAmount.x) > horizontalThreshold -> {
                    // Handle horizontal swipe (left or right)
                    if (dragAmount.x < 0f && onSwipeLeft != null) {
                        Log.d("SwipeGesture", "Swipe Left detected")
                        isNavigating.value = true
                        coroutineScope.launch {
                            onSwipeLeft()
                            delay(100)
                            isSwipeHandled.value = true
                            isNavigating.value = false
                        }
                    } else if (dragAmount.x > 0f && onSwipeRight != null) {
                        Log.d("SwipeGesture", "Swipe Right detected")
                        isNavigating.value = true
                        coroutineScope.launch {
                            onSwipeRight()
                            delay(100)
                            isSwipeHandled.value = true
                            isNavigating.value = false
                        }
                    }
                }
                abs(dragAmount.y) > verticalThreshold -> {
                    // Handle vertical swipe (up or down)
                    if (dragAmount.y < 0f && onSwipeUp != null) {
                        Log.d("SwipeGesture", "Swipe Up detected")
                        isNavigating.value = true
                        coroutineScope.launch {
                            onSwipeUp()
                            delay(100)
                            isSwipeHandled.value = true
                            isNavigating.value = false
                        }
                    } else if (dragAmount.y > 0f && onSwipeDown != null) {
                        Log.d("SwipeGesture", "Swipe Down detected")
                        isNavigating.value = true
                        coroutineScope.launch {
                            onSwipeDown()
                            delay(100)
                            isSwipeHandled.value = true
                            isNavigating.value = false
                        }
                    }
                }
            }
        }
    }
}

