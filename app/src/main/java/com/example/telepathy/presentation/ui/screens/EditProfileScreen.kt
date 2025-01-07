package com.example.telepathy.presentation.ui.screens

import android.graphics.ImageDecoder
import android.graphics.ImageDecoder.decodeBitmap
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
import androidx.navigation.NavHostController
import com.example.telepathy.R
import com.example.telepathy.data.LocalPreferences
import com.example.telepathy.data.LocalPreferences.localUser
import com.example.telepathy.presentation.ui.CircledImage
import com.example.telepathy.presentation.ui.ScreenTemplate
import com.example.telepathy.presentation.ui.Header
import com.example.telepathy.presentation.ui.theme.DarkButtonsColor
import com.example.telepathy.presentation.ui.theme.DarkUserColors

@Composable
fun EditProfileScreen(navController: NavHostController) {
    val context = LocalContext.current

    if (localUser == null) {
        LocalPreferences.createBasicLocalUser(context)
    }

    var new_username by remember { mutableStateOf(localUser?.name ?: "") }
    var new_description by remember { mutableStateOf(localUser?.description ?: "") }
    var new_selectedColor by remember { mutableStateOf(localUser?.color ?: Color.Gray) }
    var new_avatarBitmap by remember { mutableStateOf(localUser?.avatar) }
    var colorPickerVisible by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = ImageDecoder.createSource(context.contentResolver, it)
            new_avatarBitmap = decodeBitmap(bitmap)
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
                        navController.popBackStack() // Navigate back
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
                        // Save the updated user data
//                        localUser!!.name = new_username
//                        localUser!!.description = new_description
//                        localUser!!.color = new_selectedColor
//                        localUser!!.avatar = new_avatarBitmap
                        navController.popBackStack()
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
            Box( //centering box
                modifier = Modifier
                    .fillMaxWidth()
                    .size(176.dp),
                contentAlignment = Alignment.Center
            ) {
                Box( //clickable box
                    modifier = Modifier
                        .size(176.dp)
                        .background(MaterialTheme.colorScheme.background, CircleShape)
                        .clickable { imagePickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    new_avatarBitmap?.let {
                        CircledImage(bitmap = it, size = 176.dp)
                    }
                }
            }

            TextFieldComposable(
                label = stringResource(R.string.username),
                text = new_username,
                charLimit = 20,
                onTextChange = { new_username = it },
                height = 56.dp
            )

            TextFieldComposable(
                label = stringResource(R.string.description),
                text = new_description,
                charLimit = 40,
                onTextChange = { new_description = it },
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
                        .background(new_selectedColor, CircleShape)
                        .clickable { colorPickerVisible = true },
                    contentAlignment = Alignment.Center
                ) {}
            }
        }
    }

    // Color picker dialog
    if (colorPickerVisible) {
        ColorPickerDialog(
            currentColor = new_selectedColor,
            onSave = { selectedColor ->
                new_selectedColor = selectedColor
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
                style = TextStyle(fontSize = 16.sp, color = Color.DarkGray, fontWeight = FontWeight.Medium),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(2.dp)
            )

            // Character count in top-right
            BasicText(
                text = "$charactersLeft",
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(2.dp)
            )
        }
    }
}


