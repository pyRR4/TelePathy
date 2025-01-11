package com.example.telepathy.presentation.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.telepathy.presentation.ui.CircledImage
import com.example.telepathy.presentation.ui.CustomButton
import com.example.telepathy.R
import com.example.telepathy.presentation.ui.FooterWithPromptBar
import com.example.telepathy.presentation.ui.Header
import com.example.telepathy.presentation.ui.ScreenTemplate
import androidx.navigation.NavHostController
import com.example.telepathy.data.PreferencesManager
import com.example.telepathy.presentation.navigation.swipeToNavigate
import com.example.telepathy.presentation.ui.theme.AlertRed
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.request.ImageRequest
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import android.media.MediaPlayer
import android.util.Log
import com.example.telepathy.presentation.viewmodels.SharedViewModel


data class SettingOption(
    val iconBitmap: Bitmap? = null,
    val iconColor: Color? = null,
    val title: String,
    val backgroundColor: Color,
    val onClick: () -> Unit
)

@Composable
fun SettingsScreen(
    navController: NavHostController,
    currentScreen: MutableState<String>,
    sharedViewModel: SharedViewModel
) {
    val preferencesManager = PreferencesManager(LocalContext.current)
    val localUserId = preferencesManager.getLocalUserId()
    val localUser by sharedViewModel.localUser.collectAsState()

    LaunchedEffect(localUser) {
        Log.d("LOCAL USER ID", localUserId.toString())
        sharedViewModel.loadLocalUser(localUserId)
    }

    val settingsOptions = listOf(
        SettingOption(
            iconBitmap = localUser?.avatar,
            iconColor = MaterialTheme.colorScheme.secondary,
            title = stringResource(R.string.edit_profile),
            backgroundColor = Color.Gray,
            onClick = { navController.navigate("edit_profile") }
        ),
        SettingOption(
            iconColor = MaterialTheme.colorScheme.secondary,
            title = stringResource(R.string.change_pin),
            backgroundColor = Color.Gray,
            onClick = {
                val pin = preferencesManager.getPin()

                if (pin == null) {
                    navController.navigate("enter_new_pin")
                } else {
                    navController.navigate("enter_pin_settings")
                }
            }
        ),
        SettingOption(
            iconColor = MaterialTheme.colorScheme.secondary,
            title = stringResource(R.string.reset_app_data),
            backgroundColor = AlertRed,
            onClick = {} //{ navController.navigate("reset_app")}
        )
    )

    ScreenTemplate(
        navIcon = {
            FooterWithPromptBar("settingsscreen")
        },
        header = {
            Header(stringResource(R.string.settings), modifier = Modifier.padding(bottom = 16.dp))
        },
        modifier = Modifier.swipeToNavigate(
            onSwipeDown =  {
                navController.navigate(currentScreen.value)
            },
            coroutineScope = rememberCoroutineScope(),
            isNavigating = remember { mutableStateOf(false) },
            isSwipeHandled = remember { mutableStateOf(false) }
        )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            settingsOptions.forEach { option ->
                if (option.title == stringResource(R.string.reset_app_data)) {
                    GifBackgroundButton(
                        text = option.title,
                        resourceId = R.drawable.matrix,
                        onClick = option.onClick,
                        modifier = Modifier
                    )
                } else {
                    CustomButton(
                        name = option.title,
                        backgroundColor = option.backgroundColor,
                        image = {
                            if (option.iconBitmap != null) {
                                CircledImage(bitmap = option.iconBitmap, size = 48.dp)
                            } else if (option.iconColor != null) {
                                CircledImage(
                                    bitmap = null,
                                    size = 48.dp,
                                    defaultColor = option.iconColor
                                )
                            }
                        },
                        onClick = option.onClick
                    )
                }
            }

        }
    }
}


@Composable
fun ButtonIcon(image: Painter, modifier: Modifier) {
    CircledImage(null, modifier, 48.dp)
}


@Composable
fun GifBackgroundButton(
    text: String,
    resourceId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val gifPainter = rememberAsyncImagePainter(
        ImageRequest.Builder(context)
            .data(resourceId)
            .decoderFactory(GifDecoder.Factory())
            .build()
    )

    Box(
        modifier = modifier
            .clickable {
                val mediaPlayer = MediaPlayer.create(context, R.raw.glitch)
                mediaPlayer?.start()
                mediaPlayer?.setOnCompletionListener { mp -> mp.release() }

                onClick()
            }
            .fillMaxWidth()
            .height(82.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        Image(
            painter = gifPainter,
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )

        Text(
            text = text,
            color = Color.White,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Black,
                fontSize = 48.sp
            ),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
