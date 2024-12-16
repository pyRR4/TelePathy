package com.example.telepathy.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.telepathy.presentation.ui.CustomButton
import com.example.telepathy.presentation.ui.CircledImage
import com.example.telepathy.R
import com.example.telepathy.presentation.ui.DividerWithImage
import com.example.telepathy.presentation.ui.Header
import com.example.telepathy.presentation.ui.ScreenTemplate

data class EditOption(
    val icon: Int,
    val title: String,
    val backgroundColor: Color,
    val onClick: () -> Unit
)

@Composable
fun EditProfileScreen(navController: NavHostController) {
    val settingsOptions = listOf(
        EditOption(
            icon = R.drawable.test1,
            title = stringResource(R.string.change_avatar),
            backgroundColor = Color.Gray,
            onClick = { /* Navigate to avatar change screen */ }
        ),
        EditOption(
            icon = R.drawable.test1,
            title = stringResource(R.string.change_desc),
            backgroundColor = Color.Gray,
            onClick = { /* Navigate to description edit screen */ }
        ),
        EditOption(
            icon = R.drawable.test1,
            title = stringResource(R.string.change_color),
            backgroundColor = Color.Gray,
            onClick = { /* Open color picker dialog */ }
        )
    )

    ScreenTemplate(
        navIcon = {
            DividerWithImage()
        },
        header = {
            Header(stringResource(R.string.edit_profile), modifier = Modifier.padding(bottom = 16.dp))
        },
        modifier = Modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            CircledImage( // Avatar
                bitmap = null,
                modifier = Modifier,
                size = 128.dp
            )

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
                    onClick = option.onClick // Pass the specific click action
                )
            }
        }
    }
}
