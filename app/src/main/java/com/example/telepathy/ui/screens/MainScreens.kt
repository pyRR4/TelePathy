package com.example.telepathy.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.telepathy.ui.users.UsersRepository

@Composable
fun MainScreen(
    navController: NavHostController,
    userRepository: UsersRepository,
    context: Context,
    currentScreen: MutableState<String>
) {

    val horizontalScreens = listOf("available", "contacts")
    val verticalScreens = listOf("contacts", "settings")
    val horizontalPagerState = rememberPagerState(initialPage = 1, pageCount = { horizontalScreens.size })
    val verticalPagerState = rememberPagerState(initialPage = 0, pageCount = { verticalScreens.size })

    Box(modifier = Modifier.fillMaxSize()) {
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
                            "available" ->  {
                                AvailableAroundScreen(navController, userRepository.getUsers(context), currentScreen)
                                currentScreen.value = "available"
                            }
                            "contacts" -> {
                                ContactsScreen(navController, userRepository.getUsers(context), currentScreen)
                                currentScreen.value = "contacts"
                            }
                        }
                    }
            }
        }

        CustomPagerIndicator(
            pagerState = horizontalPagerState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}



@Composable
fun CustomPagerIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    activeColor: Color = Color.Blue,
    inactiveColor: Color = Color.Gray,
    indicatorSize: Dp = 8.dp,
    spacing: Dp = 4.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pagerState.pageCount) { pageIndex ->
            val isActive = pageIndex == pagerState.currentPage
            Box(
                modifier = Modifier
                    .size(if (isActive) indicatorSize * 1.5f else indicatorSize) // Większy rozmiar dla aktywnej kropki
                    .clip(CircleShape)
                    .background(if (isActive) activeColor else inactiveColor)
                    .padding(horizontal = spacing / 2)
            )
        }
    }
}