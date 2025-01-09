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
import com.example.telepathy.presentation.viewmodels.EditProfileViewModel
import com.example.telepathy.presentation.viewmodels.EditProfileViewModel.EditProfileViewModelFactory
import com.example.telepathy.presentation.ui.theme.DarkButtonsColor
import com.example.telepathy.presentation.ui.theme.DarkUserColors

@Composable
fun EditProfileScreen(
    navController: NavHostController,
    viewModel: EditProfileViewModel = viewModel(
        factory = EditProfileViewModelFactory(LocalContext.current)
    )
) {
    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)
    val localUserId = preferencesManager.getLocalUserId()

    val localUser by viewModel.user.collectAsState()

    var colorPickerVisible by remember { mutableStateOf(false) }

    var newUsername by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }
    var newSelectedColor by remember { mutableStateOf(Color.Gray) }
    var newAvatarBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(localUser) {
        viewModel.loadUser(localUserId)
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

                            viewModel.updateUser(updatedUser)
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .height(72.dp)
                        .width(160.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkButtonsColor),
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
                        .background(MaterialTheme.colorScheme.background, CircleShape)
                        .clickable { imagePickerLauncher.launch("image/*") },
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

