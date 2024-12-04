package com.example.telepathy.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.telepathy.ui.CircledImage
import com.example.telepathy.ui.CustomButton
import com.example.telepathy.R
import com.example.telepathy.ui.BottomNavBar
import com.example.telepathy.ui.DividerWithImage
import com.example.telepathy.ui.Header
import com.example.telepathy.ui.ScreenTemplate
import com.example.telepathy.ui.utils.swipeToNavigate

data class SettingOption(
    val icon: Int,
    val title: String,
    val backgroundColor: Color
)


@Composable
fun SettingsScreen(navController: NavHostController, currentScreen: MutableState<String>) {
    val settingsOptions = listOf(
        SettingOption(
            icon = R.drawable.test1, // Replace with actual icon resources
            title = stringResource(R.string.edit_profile),
            backgroundColor = Color.Gray
        ),
        SettingOption(
            icon = R.drawable.test1,
            title = stringResource(R.string.change_pin),
            backgroundColor = Color.Gray
        ),
        SettingOption(
            icon = R.drawable.test1,
            title = stringResource(R.string.reset_app_data),
            backgroundColor = Color.Gray
        )
    )

    var isSwipeHandled by remember { mutableStateOf(false) }
    var isNavigating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    ScreenTemplate(
        navIcon = {
            BottomNavBar(
                currentScreen = currentScreen.value,
                onNavigate = { targetScreen ->
                    currentScreen.value = targetScreen
                    navController.navigate(targetScreen) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        },
        header = {
            Header(stringResource(R.string.settings), modifier = Modifier.padding(bottom = 16.dp))
        },
        modifier = Modifier.swipeToNavigate(
            isSwipeHandled = remember { mutableStateOf(isSwipeHandled) },
            isNavigating = remember { mutableStateOf(isNavigating) },
            coroutineScope = coroutineScope,
            onSwipeUp = {
                navController.navigate("contacts")
                currentScreen.value = "contacts"
            }
        )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            settingsOptions.forEach { option ->
                CustomButton(
                    name = option.title,
                    backgroundColor = option.backgroundColor,
                    image = {
                        ButtonIcon(
                            image = painterResource(id = option.icon),
                            modifier = Modifier
                        )
                    },
                    onClick = { /* Handle click for this setting */ }
                )
            }
        }
    }
}



@Composable
fun ButtonIcon(image: Painter, modifier: Modifier) {
    CircledImage(null, modifier, 48.dp)
}

