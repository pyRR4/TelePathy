package com.example.telepathy.presentation.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.graphics.asImageBitmap
import androidx.navigation.NavHostController
import com.example.telepathy.presentation.ui.CustomButton
import com.example.telepathy.presentation.ui.CircledImage
import com.example.telepathy.R
import com.example.telepathy.data.LocalPreferences.localUser
import com.example.telepathy.presentation.ui.DividerWithImage
import com.example.telepathy.presentation.ui.Header
import com.example.telepathy.presentation.ui.ScreenTemplate

// Data class for EditOption
data class EditOption(
    val iconBitmap: Bitmap? = null,  // Optional Bitmap
    val iconColor: Color? = null,    // Optional Color
    val title: String,
    val backgroundColor: Color,
    val onClick: () -> Unit
)

@Composable
fun EditProfileScreen(navController: NavHostController) {
    val settingsOptions = listOf(
        EditOption(
            iconBitmap = null, // Optional bitmap, here we use color instead
            iconColor = Color.Black, // Color for the icon
            title = stringResource(R.string.change_avatar),
            backgroundColor = Color.Gray,
            onClick = { /* Navigate to avatar change screen */ }
        ),
        EditOption(
            iconBitmap = null,
            iconColor = Color.Black,
            title = stringResource(R.string.change_desc),
            backgroundColor = Color.Gray,
            onClick = { /* Navigate to description edit screen */ }
        ),
        EditOption(
            iconBitmap = null,
            iconColor = localUser?.color, // powino byc zawsze
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
            CircledImage( // Avatar (no bitmap, using default color)
                bitmap = null,
                modifier = Modifier,
                size = 128.dp
            )

            settingsOptions.forEach { option ->
                CustomButton(
                    name = option.title,
                    backgroundColor = option.backgroundColor,
                    image = {
                        if (option.iconBitmap != null) {
                            // If bitmap exists, display it
                            CircledImage(bitmap = option.iconBitmap, size = 48.dp)
                        } else if (option.iconColor != null) {
                            // If color exists, display a circle with that color
                            CircledImage(bitmap = null, size = 48.dp, defaultColor = option.iconColor)
                        }
                    },
                    onClick = option.onClick // Pass the specific click action
                )
            }
        }
    }
}
