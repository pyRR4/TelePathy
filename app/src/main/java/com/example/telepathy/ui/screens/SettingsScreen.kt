package com.example.telepathy.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.telepathy.ui.CicrcledImage
import com.example.telepathy.ui.CustomButton
import com.example.telepathy.R
import com.example.telepathy.ui.DividerWithImage
import com.example.telepathy.ui.ScreenTemplate

data class SettingOption(
    val icon: Int,
    val title: String,
    val backgroundColor: Color
)


@Composable
fun SettingsScreen(navController: NavHostController) {
    val settingsOptions = listOf(
        SettingOption(
            icon = R.drawable.test, // Replace with actual icon resources
            title = stringResource(R.string.edit_profile),
            backgroundColor = Color.Gray
        ),
        SettingOption(
            icon = R.drawable.test,
            title = stringResource(R.string.change_pin),
            backgroundColor = Color.DarkGray
        ),
        SettingOption(
            icon = R.drawable.test,
            title = stringResource(R.string.reset_app_data),
            backgroundColor = Color.Red
        )
    )

    ScreenTemplate(
        title = stringResource(R.string.settings),
        navIcon = { DividerWithImage() }
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
    CicrcledImage(image, modifier, 48.dp)
}

