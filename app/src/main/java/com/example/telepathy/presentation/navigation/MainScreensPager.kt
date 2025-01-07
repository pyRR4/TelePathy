package com.example.telepathy.presentation.navigation

import android.content.Context
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import com.example.telepathy.presentation.ui.screens.AvailableAroundScreen
import com.example.telepathy.presentation.ui.screens.ContactsScreen
import com.example.telepathy.presentation.ui.screens.SettingsScreen


@Composable
fun MainScreensPager(
    horizontalScreens: List<String>,
    verticalScreens: List<String>,
    horizontalPagerState: PagerState,
    verticalPagerState: PagerState,
    navController: NavHostController,
    currentScreen: MutableState<String>,
    localUserId: Int,
    context: Context
) {
    VerticalPager(
        state = verticalPagerState
    ) { page ->
        when (verticalScreens[page]) {
            "settings" -> SettingsScreen(
                navController
            )

            "contacts" ->
                HorizontalPager(
                    state = horizontalPagerState
                ) { horizontalPage ->
                    when (horizontalScreens[horizontalPage]) {
                        "available" -> {
                            AvailableAroundScreen(
                                navController = navController,
                                currentScreen = currentScreen
                            )
                            currentScreen.value = "available"
                        }

                        "contacts" -> {
                            ContactsScreen(
                                navController = navController,
                                localUserId = localUserId,
                                currentScreen = currentScreen
                            )
                            currentScreen.value = "contacts"
                        }
                    }
                }
        }
    }
}