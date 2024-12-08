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
import com.example.telepathy.domain.users.UsersRepository

@Composable
fun MainScreensPager(
    horizontalScreens: List<String>,
    verticalScreens: List<String>,
    horizontalPagerState: PagerState,
    verticalPagerState: PagerState,
    navController: NavHostController,
    currentScreen: MutableState<String>,
    userRepository: UsersRepository,
    context: Context
) {
    VerticalPager(
        state = verticalPagerState
    ) { page ->
        when (verticalScreens[page]) {
            "settings" -> SettingsScreen()

            "contacts" ->
                HorizontalPager(
                    state = horizontalPagerState
                ) { horizontalPage ->
                    when (horizontalScreens[horizontalPage]) {
                        "available" -> {
                            AvailableAroundScreen(
                                navController,
                                userRepository.getUsers(context),
                                currentScreen
                            )
                            currentScreen.value = "available"
                        }

                        "contacts" -> {
                            ContactsScreen(
                                navController,
                                userRepository.getUsers(context),
                                currentScreen
                            )
                            currentScreen.value = "contacts"
                        }
                    }
                }
        }
    }
}