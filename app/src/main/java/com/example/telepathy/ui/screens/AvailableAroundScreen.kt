package com.example.telepathy.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.telepathy.ui.ScreenTemplate
import com.example.telepathy.R
import com.example.telepathy.ui.CustomButton
import com.example.telepathy.ui.DividerWithImage
import com.example.telepathy.ui.swipeToNavigate


@Composable
fun AvailableAroundScreen(navController: NavHostController, availableContacts: List<Contact>) {
    var isSwipeHandled by remember { mutableStateOf(false) }
    var isNavigating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    ScreenTemplate(
        title = stringResource(R.string.available_around_you),
        navIcon = { DividerWithImage() },
        modifier = Modifier.swipeToNavigate(
            isSwipeHandled = remember { mutableStateOf(isSwipeHandled) },
            isNavigating = remember { mutableStateOf(isNavigating) },
            coroutineScope = coroutineScope,
            onSwipeLeft = {
                Log.d("Navigation", "Navigating to Contacts from Available Around")
                navController.navigate("contacts")
            },
            onSwipeDown = {
                Log.d("Navigation", "Navigating to Settings from Available Around")
                navController.navigate("settings")
            }
        )
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(availableContacts.size) { index ->
                val contact = availableContacts[index]
                CustomButton (
                    name = contact.name,
                    image = {
                        ButtonIcon(
                            image = painterResource(contact.imageDrawable),
                            modifier = Modifier
                        )
                    },
                    backgroundColor = contact.backgroundColor,
                    onClick = { /* Handle click */ }
                )
            }
        }
    }
}

