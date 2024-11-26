package com.example.telepathy.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.telepathy.ui.DividerWithImage
import com.example.telepathy.ui.ScreenTemplate
import com.example.telepathy.ui.utils.swipeToNavigate

data class SettingOption(
    val icon: Int,
    val title: String,
    val backgroundColor: Color
)


@Composable
fun SettingsScreen(navController: NavHostController) {
    val settingsOptions = listOf(
        SettingOption(
            icon = R.drawable.test1, // Replace with actual icon resources
            title = stringResource(R.string.edit_profile),
            backgroundColor = Color.Gray
        ),
        SettingOption(
            icon = R.drawable.test1,
            title = stringResource(R.string.change_pin),
            backgroundColor = Color.DarkGray
        ),
        SettingOption(
            icon = R.drawable.test1,
            title = stringResource(R.string.reset_app_data),
            backgroundColor = Color.Red
        )
    )

    var isSwipeHandled by remember { mutableStateOf(false) }
    var isNavigating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    ScreenTemplate(
        title = stringResource(R.string.settings),
        navIcon = { DividerWithImage() },
        modifier = Modifier.swipeToNavigate(
            isSwipeHandled = remember { mutableStateOf(isSwipeHandled) },
            isNavigating = remember { mutableStateOf(isNavigating) },
            coroutineScope = coroutineScope,
            onSwipeUp = { navController.navigate("available") }
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

