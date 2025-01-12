package com.example.telepathy.presentation.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.telepathy.R
import com.example.telepathy.data.PreferencesManager
import com.example.telepathy.presentation.ui.CircledImage
import com.example.telepathy.presentation.ui.ScreenTemplate
import com.example.telepathy.presentation.ui.Header
import com.example.telepathy.presentation.ui.theme.DarkButtonsColor
import com.example.telepathy.presentation.ui.theme.DarkDeepPurple
import com.example.telepathy.presentation.ui.theme.DarkUserColors
import com.example.telepathy.presentation.viewmodels.SharedViewModel

@Composable
fun EditProfileScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    val context = LocalContext.current
    val localUser by sharedViewModel.localUser.collectAsState()

    val preferencesManager = PreferencesManager(context)
    val localUserId = preferencesManager.getLocalUserId()

    var colorPickerVisible by remember { mutableStateOf(false) }
    var avatarPickerVisible by remember { mutableStateOf(false) }

    var newUsername by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }
    var newSelectedColor by remember { mutableStateOf(Color.Gray) }
    var newAvatarBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(localUser) {
        sharedViewModel.loadLocalUser(localUserId)
        localUser?.let { user ->
            if(!isLoaded) {
                newUsername = user.name
                newDescription = user.description
                newSelectedColor = user.color
                newAvatarBitmap = user.avatar
                isLoaded = true
            }
        }
    }

//    val imagePickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        uri?.let {
//            try {
//                val source = ImageDecoder.createSource(context.contentResolver, it)
//                val bitmap = decodeBitmap(source)
//                new_avatarBitmap = bitmap
//            } catch (e: Exception) {
//                Log.e("ImagePicker", "Failed to decode image: ${e.message}")
//            }
//        }
//    }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            newAvatarBitmap = bitmap
        }
    }

    ScreenTemplate(
        navIcon = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Button(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .height(72.dp)
                        .width(160.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkButtonsColor),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = stringResource(R.string.cancel), fontSize = 22.sp)
                }

                Button(
                    onClick = {
                        localUser?.let { existingUser ->
                            val updatedUser = existingUser.copy(
                                name = newUsername,
                                description = newDescription,
                                color = newSelectedColor,
                                avatar = newAvatarBitmap
                            )

                            sharedViewModel.updateLocalUser(updatedUser)
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .height(72.dp)
                        .width(160.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkDeepPurple),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = stringResource(R.string.save), fontSize = 22.sp, color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        },
        header = {
            Header(
                stringResource(R.string.edit_profile),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        },
        modifier = Modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(176.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(176.dp)
                        .background(Color.Black, CircleShape)
                        .clickable { avatarPickerVisible=true },
                    contentAlignment = Alignment.Center
                ) {
                    newAvatarBitmap?.let {
                        CircledImage(bitmap = it, size = 176.dp)
                    }
                }
            }

            TextFieldComposable(
                label = stringResource(R.string.username),
                text = newUsername,
                charLimit = 20,
                onTextChange = { newUsername = it },
                height = 56.dp
            )

            TextFieldComposable(
                label = stringResource(R.string.description),
                text = newDescription,
                charLimit = 40,
                onTextChange = { newDescription = it },
                height = 124.dp
            )

            // Color Circle
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.color),
                    style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(newSelectedColor, CircleShape)
                        .clickable { colorPickerVisible = true },
                    contentAlignment = Alignment.Center
                ) {}
            }
        }
    }

    if (colorPickerVisible) {
        ColorPickerDialog(
            currentColor = newSelectedColor,
            onSave = { selectedColor ->
                newSelectedColor = selectedColor
                colorPickerVisible = false
            }
        )
    }

    if (avatarPickerVisible) {
        AvatarPickerDialog(
            onSave = { selectedBitmap ->
                newAvatarBitmap = selectedBitmap
            },
            onUploadImage = {
                avatarPickerVisible = false
                imagePickerLauncher.launch("image/*")
            },
            onDismiss = { avatarPickerVisible = false }
        )
    }
}



@Composable
fun ColorPickerDialog(
    currentColor: Color,
    onSave: (Color) -> Unit
) {
    var new_selectedColor by remember { mutableStateOf(currentColor) }

    Dialog(onDismissRequest = {}) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .background(
                    Color.White,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(24.dp)
        ) {
            // Header
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Choose a Color",
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }

            // Colors grid
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                DarkUserColors.chunked(4).forEach { rowColors ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        rowColors.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .background(color, shape = CircleShape)
                                    .border(
                                        width = if (new_selectedColor == color) 6.dp else 0.dp,
                                        color = if (new_selectedColor == color) Color.Black else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        new_selectedColor = color
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

@Composable
fun TextFieldComposable(
    label: String,
    text: String,
    charLimit: Int,
    onTextChange: (String) -> Unit,
    height: androidx.compose.ui.unit.Dp
) {
    var charactersLeft by remember { mutableStateOf(charLimit - text.length) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TextField(
                value = text,
                onValueChange = {
                    if (it.length <= charLimit) {
                        onTextChange(it)
                        charactersLeft = charLimit - it.length
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .background(Color(0xFFADADAD), RoundedCornerShape(10.dp)),
                textStyle = TextStyle(fontSize = 20.sp),
                placeholder = {
                    BasicText("Enter your text here...", style = TextStyle(color = Color.Gray))
                }
            )

            // Label in top-left
            Text(
                text = label,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(2.dp)
            )

            // Character count in top-right
            BasicText(
                text = "$charactersLeft",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                ),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(2.dp)
            )
        }
    }
}

@Composable
fun AvatarPickerDialog(
    onSave: (Bitmap) -> Unit,
    onUploadImage: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val avatars = (1..15).mapNotNull { id ->
        val resId = context.resources.getIdentifier("av$id", "drawable", context.packageName)
        if (resId != 0) {
            val originalBitmap = BitmapFactory.decodeResource(context.resources, resId)
            originalBitmap?.let {
                // Skalowanie obrazu do maksymalnych wymiarów 512x512 px
                val maxDimension = 512
                val ratio = minOf(
                    maxDimension / it.width.toFloat(),
                    maxDimension / it.height.toFloat()
                )
                val scaledWidth = (it.width * ratio).toInt()
                val scaledHeight = (it.height * ratio).toInt()
                Bitmap.createScaledBitmap(it, scaledWidth, scaledHeight, true)
            }
        } else {
            Log.e("AvatarPicker", "Resource av$id not found")
            null
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(24.dp)
        ) {
            // Header
            Text(
                text = "Choose an Avatar",
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Avatar grid
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                avatars.chunked(3).forEach { rowAvatars ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        rowAvatars.forEach { avatar ->
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(Color.LightGray)
                                    .clickable {
                                        onSave(avatar)
                                        onDismiss()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                CircledImage(bitmap = avatar, size = 90.dp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Upload Image button
            Button(
                onClick = onUploadImage,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = DarkButtonsColor)
            ) {
                Text(text = "Upload Your Image", fontSize = 18.sp, color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}
