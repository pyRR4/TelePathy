package com.example.telepathy.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.telepathy.ui.ScreenTemplate
import com.example.telepathy.R
import com.example.telepathy.clases.User
import com.example.telepathy.ui.BottomNavBar
import com.example.telepathy.ui.CustomButton
import com.example.telepathy.ui.Header
import com.example.telepathy.ui.utils.swipeToNavigate


@Composable
fun AvailableAroundScreen(navController: NavHostController, availableContacts: List<User>, currentScreen: MutableState<String>) {
    var isSwipeHandled by remember { mutableStateOf(false) }
    var isNavigating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    ScreenTemplate(
        navIcon = {
            BottomNavBar(
                currentScreen = currentScreen.value,
                onNavigate = { targetScreen ->
                    currentScreen.value = targetScreen
                    navController.navigate(targetScreen) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        },
        header = {
            Header(stringResource(R.string.available_around_you), modifier = Modifier.padding(bottom = 16.dp))
        },
        modifier = Modifier.swipeToNavigate(
            isSwipeHandled = remember { mutableStateOf(isSwipeHandled) },
            isNavigating = remember { mutableStateOf(isNavigating) },
            coroutineScope = coroutineScope,
            onSwipeLeft = {
                Log.d("Navigation", "Navigating to Contacts from Available Around")
                navController.navigate("contacts")
                currentScreen.value = "contacts"
            },
            onSwipeDown = {
                Log.d("Navigation", "Navigating to Settings from Available Around")
                navController.navigate("settings")
                currentScreen.value = "settings"
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
                            image = painterResource(R.drawable.test1),
                            modifier = Modifier
                        )
                    },
                    backgroundColor = contact.color,
                    onClick = { /* Handle click */ }
                )
            }
        }
    }
}

