package com.example.telepathy.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.telepathy.ui.ScreenTemplate
import com.example.telepathy.ui.theme.TelePathyTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.telepathy.R
import com.example.telepathy.ui.DividerWithImage
import com.example.telepathy.ui.swipeToNavigateBack


@Composable
fun AvailableAroundScreen(navController: NavHostController, availableContacts: List<Contact>) {
    var isSwipeHandled by remember { mutableStateOf(false) }
    var isNavigating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    ScreenTemplate(
        title = stringResource(R.string.available_around_you),
        navIcon = { DividerWithImage() }
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .swipeToNavigateBack(
                    navController = navController,
                    isSwipeHandled = remember { mutableStateOf(isSwipeHandled) },
                    isNavigating = remember { mutableStateOf(isNavigating) },
                    coroutineScope = coroutineScope
                )
        ) {
            items(availableContacts.size) { index ->
                val contact = availableContacts[index]
                ContactCard(
                    imageDrawable = contact.imageDrawable,
                    name = contact.name,
                    isFromUser = false,
                    message = "",
                    time = "",
                    backgroundColor = contact.backgroundColor,
                    onClick = { /* Handle click */ }
                )
            }
        }
    }
}

