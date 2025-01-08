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
import com.example.telepathy.data.AppDatabase
import com.example.telepathy.data.PreferencesManager
import com.example.telepathy.data.entities.User
import kotlinx.coroutines.flow.firstOrNull
import com.example.telepathy.presentation.navigation.swipeToNavigate
import com.example.telepathy.presentation.ui.theme.AlertRed

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
    currentScreen: MutableState<String>
) {
    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)
    val localUserId = preferencesManager.getLocalUserId()
    val database = AppDatabase.getDatabase(context)

    var localUser by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(Unit) {
        localUser = database.userDao().getUser(localUserId).firstOrNull()
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
            onClick = { navController.navigate("reset_app")}
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
}


@Composable
fun ButtonIcon(image: Painter, modifier: Modifier) {
    CircledImage(null, modifier, 48.dp)
}
