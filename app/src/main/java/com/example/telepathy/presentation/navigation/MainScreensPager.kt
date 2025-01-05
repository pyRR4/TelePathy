package com.example.telepathy.presentation.navigation

import android.content.Context
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.telepathy.data.AppDatabase
import com.example.telepathy.data.repositories.MessageRepositoryImpl
import com.example.telepathy.data.repositories.UserRepositoryImpl
import com.example.telepathy.presentation.ui.screens.AvailableAroundScreen
import com.example.telepathy.presentation.ui.screens.ContactsScreen
import com.example.telepathy.presentation.ui.screens.SettingsScreen
import androidx.compose.ui.platform.LocalContext


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
            "settings" -> SettingsScreen(navController)

            "contacts" ->
                HorizontalPager(
                    state = horizontalPagerState
                ) { horizontalPage ->
                    when (horizontalScreens[horizontalPage]) {
                        "available" -> {
                            AvailableAroundScreen(
                                navController,
                                viewModel(),
                                currentScreen
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