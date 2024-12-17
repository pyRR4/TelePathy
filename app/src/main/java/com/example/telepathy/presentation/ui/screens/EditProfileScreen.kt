package com.example.telepathy.presentation.ui.screens

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.telepathy.R
import com.example.telepathy.data.LocalPreferences
import com.example.telepathy.data.LocalPreferences.localUser
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.telepathy.presentation.ui.*
import com.example.telepathy.presentation.ui.theme.UserColors

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

    if (localUser == null) {
        LocalPreferences.createBasicLocalUser(context)
    }

    var isColorPickerDialogVisible by remember { mutableStateOf(false) }

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            localUser?.avatar = bitmap // Update local user data
        }
    }

    // Edit options
    val settingsOptions = listOf(
        EditOption(
            iconBitmap = localUser?.avatar,
            title = stringResource(R.string.change_avatar),
            backgroundColor = Color.Gray,
            onClick = { imagePickerLauncher.launch("image/*") }
        ),
        EditOption(
            title = stringResource(R.string.change_name),
            backgroundColor = Color.Gray,
            onClick = { navController.navigate("change_name") }
        ),
        EditOption(
            title = stringResource(R.string.change_desc),
            backgroundColor = Color.Gray,
            onClick = { navController.navigate("change_desc") }
        ),
        EditOption(
            iconColor = localUser?.color,
            title = stringResource(R.string.change_color),
            backgroundColor = Color.Gray,
            onClick = { isColorPickerDialogVisible = true }
        )
    )

    ScreenTemplate(
        navIcon = { DividerWithImage() },
        header = {
            Header(
                text = localUser?.name ?: stringResource(R.string.no_username),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        },
        modifier = Modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Display avatar
            CircledImage(
                bitmap = localUser?.avatar,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                size = 128.dp
            )

            // Display settings options
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
                    onClick = option.onClick
                )
            }
        }
    }

    // Color picker dialog
    if (isColorPickerDialogVisible) {
        ColorPickerDialog(
            currentColor = localUser?.color ?: Color.Black,
            onSave = { selectedColor ->
                localUser?.color = selectedColor // Update local user data
                isColorPickerDialogVisible = false
            }
        )
    }
}

@Composable
fun ColorPickerDialog(
    currentColor: Color,
    onSave: (Color) -> Unit
) {
    var selectedColor by remember { mutableStateOf(currentColor) }

    Dialog(onDismissRequest = {}) {
        Column(
            modifier = Modifier
                .padding(24.dp) // Larger padding for better spacing
                .background(Color.White, shape = RoundedCornerShape(16.dp)) // Slightly larger rounded corners
                .padding(24.dp) // Increased inner padding
        ) {
            // Header
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center // Center the header text
            ) {
                BasicText(
                    text = "Choose a Color",
                    style = TextStyle(
                        fontSize = 24.sp, // Slightly larger font size
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 24.dp) // Add spacing below the header
                )
            }

            // Color grid using UserColors
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp), // Increase spacing between rows
                modifier = Modifier.fillMaxWidth()
            ) {
                UserColors.chunked(4).forEach { rowColors -> // Adjusted to 4 colors per row for better sizing
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp), // Increased spacing between colors
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        rowColors.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(color, shape = CircleShape)
                                    .border(
                                        width = if (selectedColor == color) 6.dp else 0.dp, // Thicker border for selected color
                                        color = if (selectedColor == color) Color.Black else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        selectedColor = color
                                        onSave(color)
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}
