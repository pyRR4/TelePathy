package com.example.telepathy.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.telepathy.presentation.ui.ScreenTemplate
import com.example.telepathy.R
import com.example.telepathy.presentation.navigation.swipeToNavigate
import com.example.telepathy.presentation.ui.CustomButton
import com.example.telepathy.presentation.ui.FooterWithPromptBar
import com.example.telepathy.presentation.ui.Header
import com.example.telepathy.presentation.viewmodels.ContactsViewModel
import com.example.telepathy.presentation.viewmodels.ContactsViewModelFactory


@Composable
fun AvailableAroundScreen(
    navController: NavHostController,
    viewModel: ContactsViewModel = viewModel(
        factory = ContactsViewModelFactory(LocalContext.current)
    ),
    currentScreen: MutableState<String>
) {

    val contacts by viewModel.contacts.collectAsState()

    val availableContacts = contacts.keys.toList()

    ScreenTemplate(
        navIcon = {
            FooterWithPromptBar(currentScreen.value)
        },
        header = {
            Header(stringResource(R.string.available_around_you), modifier = Modifier.padding(bottom = 16.dp))
        },
        modifier = Modifier.swipeToNavigate(
            onSwipeLeft =  {
                navController.navigate("contactsscreen")
            },
            onSwipeUp = {
                navController.navigate("settingsscreen")
            },
            coroutineScope = rememberCoroutineScope(),
            isNavigating = remember { mutableStateOf(false) },
            isSwipeHandled = remember { mutableStateOf(false) }
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
                    onClick = { navController.navigate("talkscreen/${contact.id}") }
                )
            }
        }
    }
}

