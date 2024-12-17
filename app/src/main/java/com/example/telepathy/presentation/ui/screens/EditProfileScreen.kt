package com.example.telepathy.presentation.ui.screens

import android.graphics.Bitmap
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.navigation.NavHostController
import com.example.telepathy.presentation.ui.CustomButton
import com.example.telepathy.presentation.ui.CircledImage
import com.example.telepathy.R
import com.example.telepathy.data.LocalPreferences.localUser
import com.example.telepathy.presentation.ui.DividerWithImage
import com.example.telepathy.presentation.ui.Header
import com.example.telepathy.presentation.ui.ScreenTemplate
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

data class EditOption(
    val iconBitmap: Bitmap? = null,
    val iconColor: Color? = null,
    val title: String,
    val backgroundColor: Color,
    val onClick: () -> Unit
)

@Composable
fun EditProfileScreen(navController: NavHostController) {
    val context = LocalContext.current

    // Mutable state for the avatar bitmap
    var avatarBitmap by remember { mutableStateOf(localUser?.avatar?.asImageBitmap()) }

    // ActivityResultLauncher for image selection
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            avatarBitmap = bitmap.asImageBitmap()
            localUser?.avatar = bitmap // Update the user's avatar in LocalPreferences
        }
    }

    val settingsOptions = listOf(
        EditOption(
            iconBitmap = avatarBitmap?.asAndroidBitmap(),
            iconColor = null,
            title = stringResource(R.string.change_avatar),
            backgroundColor = Color.Gray,
            onClick = { imagePickerLauncher.launch("image/*") } // Launch image picker
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
            iconColor = localUser?.color,
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
            // Display the current avatar
            CircledImage(
                bitmap = avatarBitmap?.asAndroidBitmap(),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                size = 128.dp
            )

            settingsOptions.forEach { option ->
                CustomButton(
                    name = option.title,
                    backgroundColor = option.backgroundColor,
                    image = {
                        if (option.iconBitmap != null) {
                            CircledImage(bitmap = option.iconBitmap, size = 48.dp)
                        } else if (option.iconColor != null) {
                            CircledImage(bitmap = null, size = 48.dp, defaultColor = option.iconColor)
                        }
                    },
                    onClick = option.onClick // Pass the specific click action
                )
            }
        }
    }
}
