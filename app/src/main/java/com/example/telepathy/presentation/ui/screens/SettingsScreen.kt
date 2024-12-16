package com.example.telepathy.presentation.ui.screens

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
import com.example.telepathy.presentation.ui.CircledImage
import com.example.telepathy.presentation.ui.CustomButton
import com.example.telepathy.R
import com.example.telepathy.presentation.ui.DividerWithImage
import com.example.telepathy.presentation.ui.Header
import com.example.telepathy.presentation.ui.ScreenTemplate
import androidx.navigation.NavHostController

data class SettingOption(
    val icon: Int,
    val title: String,
    val backgroundColor: Color,
    val onClick: () -> Unit
)

@Composable
fun SettingsScreen(navController: NavHostController) {
    val settingsOptions = listOf(
        SettingOption(
            icon = R.drawable.test1, // Replace with actual icon resources
            title = stringResource(R.string.edit_profile),
            backgroundColor = Color.Gray,
            onClick = {navController.navigate("edit_profile")}
        ),
        SettingOption(
            icon = R.drawable.test1,
            title = stringResource(R.string.change_pin),
            backgroundColor = Color.Gray,
            onClick = {navController.navigate("enter_pin")}
        ),
        SettingOption(
            icon = R.drawable.test1,
            title = stringResource(R.string.reset_app_data),
            backgroundColor = Color.Gray,
            onClick = {navController.navigate("reset_app")}
        )
    )

    ScreenTemplate(
        navIcon = {
            DividerWithImage()
        },
        header = {
            Header(stringResource(R.string.settings), modifier = Modifier.padding(bottom = 16.dp))
        },
        modifier = Modifier
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
                    onClick = option.onClick
                )
            }
        }
    }
}

@Composable
fun ButtonIcon(image: Painter, modifier: Modifier) {
    CircledImage(null, modifier, 48.dp)
}
