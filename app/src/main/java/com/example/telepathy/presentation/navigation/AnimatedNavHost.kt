package com.example.telepathy.presentation.navigation

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
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.*
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.telepathy.presentation.ui.screens.AvailableAroundScreen
import com.example.telepathy.presentation.ui.screens.TalkScreen
import com.example.telepathy.presentation.ui.screens.ConfirmPinScreen
import com.example.telepathy.presentation.ui.screens.ContactsScreen
import com.example.telepathy.presentation.ui.screens.EditProfileScreen
import com.example.telepathy.presentation.ui.screens.EnterNewPinScreen
import com.example.telepathy.presentation.ui.screens.EnterPinScreen
import com.example.telepathy.presentation.ui.screens.SettingsScreen
import kotlin.math.abs


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedNavHost(
    navController: NavHostController,
    startDestination: String,
    currentScreen: MutableState<String>,
    localUserDeviceId: String
) {
    androidx.navigation.compose.NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {

        composable(
            route = "contactsscreen",
            enterTransition = {
                if(initialState.destination.route?.startsWith("talkscreen/") ?: false) {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = LinearEasing
                        )
                    )
                }
                else if(initialState.destination.route?.startsWith("availablescreen") ?: false) {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = LinearEasing
                        )
                    )
                } else if(initialState.destination.route?.startsWith("settingsscreen") ?: false) {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Down,
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
                fadeOut(
                    animationSpec = tween(durationMillis = 350)
                )
            }
        ) {
            ContactsScreen(
                navController = navController,
                currentScreen = currentScreen
            )
            currentScreen.value = "contactsscreen"
        }

        composable(
            route = "availablescreen",
            enterTransition = {
                if(initialState.destination.route?.startsWith("talkscreen/") ?: false ||
                    initialState.destination.route?.startsWith("contactsscreen") ?: false) {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = LinearEasing
                        )
                    )
                } else {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Down,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = LinearEasing
                        )
                    )
                }
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(durationMillis = 350)
                )
            }
        ) {
            AvailableAroundScreen(
                navController = navController,
                currentScreen = currentScreen
            )
            currentScreen.value = "availablescreen"
        }

        composable(
            route = "settingsscreen",
            enterTransition = {
                if(initialState.destination.route?.startsWith("availablescreen") ?: false ||
                    initialState.destination.route?.startsWith("contactsscreen") ?: false) {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Up,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = LinearEasing
                        )
                    )
                } else {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Down,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = LinearEasing
                        )
                    )
                }
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(durationMillis = 350)
                )
            }
        ) {
            SettingsScreen(
                navController,
                currentScreen
            )
        }



        composable(
            route = "talkscreen/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType }),
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(
                        durationMillis = 250,
                        easing = LinearEasing
                    )
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(durationMillis = 350)
                )
            },
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable
            TalkScreen(
                navController = navController,
                localUserDeviceId = localUserDeviceId,
                remoteUserId = userId,
                previousScreen = currentScreen
            )
        }

        composable(
            route = "edit_profile",
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(
                        durationMillis = 250,
                        easing = LinearEasing
                    )
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(durationMillis = 350)
                )
            },
        ) {
            EditProfileScreen(
                navController
            )
        }

        composable(
            route = "enter_pin_login",
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(
                        durationMillis = 250,
                        easing = LinearEasing
                    )
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(durationMillis = 350)
                )
            },
        ) {
            EnterPinScreen(
                navController, "contactsscreen", onCancel = null
            )
        }

        composable(
            route = "enter_pin_settings",
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(
                        durationMillis = 250,
                        easing = LinearEasing
                    )
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(durationMillis = 350)
                )
            },
        ) {
            EnterPinScreen(navController, "enter_new_pin", onCancel = { navController.popBackStack() })
        }

        composable(
            route = "enter_new_pin",
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(
                        durationMillis = 250,
                        easing = LinearEasing
                    )
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(durationMillis = 350)
                )
            },
        ) {
            EnterNewPinScreen(
                navController = navController,
                onCancel = { navController.popBackStack() }
            )
        }

        composable(
            route = "confirm_new_pin/{pin}",
            arguments = listOf(navArgument("pin") { type = NavType.StringType }),
            enterTransition = {
                fadeIn(
                    animationSpec = tween(durationMillis = 350)
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(durationMillis = 350)
                )
            },
        ) { backStackEntry ->
            val pinTemp = backStackEntry.arguments?.getString("pin") ?: ""
            ConfirmPinScreen(
                navController = navController,
                onCancel = { navController.popBackStack() },
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
    horizontalThreshold: Float = 25f,
    verticalThreshold: Float = 25f,
): Modifier = this.pointerInput(Unit) {
    detectDragGestures { change, dragAmount ->
        change.consume()

        if (!isSwipeHandled.value && !isNavigating.value) {
            when {
                abs(dragAmount.x) > horizontalThreshold -> {
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

