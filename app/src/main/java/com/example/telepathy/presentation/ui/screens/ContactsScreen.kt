package com.example.telepathy.presentation.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telepathy.R
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.telepathy.data.PreferencesManager
import com.example.telepathy.presentation.navigation.swipeToNavigate
import com.example.telepathy.presentation.ui.CircledImage
import com.example.telepathy.presentation.ui.FooterWithPromptBar
import com.example.telepathy.presentation.ui.Header
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.telepathy.presentation.ui.ScreenTemplate
import com.example.telepathy.presentation.viewmodels.ContactsViewModel
import com.example.telepathy.presentation.viewmodels.ContactsViewModel.ContactsViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun formatTime(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val date = Date(timestamp)
    return dateFormat.format(date)
}

@Composable
fun ContactText(name: String, isFromUser: Boolean, message: String, timestamp: Long, modifier: Modifier) {
    var formattedTime = "--:--"
    if(timestamp != -1L) {
        formattedTime = formatTime(timestamp)
    }
    var msg = message
    msg = if (isFromUser) {
        "Ty:\n$msg"
    } else if (msg.equals("")){
        stringResource(R.string.no_chat_history_with_this_user)
    } else {
        "$name:\n$msg"
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = msg,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSecondary,
                lineHeight = 18.sp,
                modifier = Modifier.padding(top = 10.dp),
                overflow = TextOverflow.Ellipsis
            )
        }

        Text(
            text = formattedTime,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSecondary,
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun UserCard(
    avatarBitmap: Bitmap?,
    name: String,
    isFromUser: Boolean,
    message: String,
    time: Long,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(124.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    )
    {
        val buttonModifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onClick)
            .background(color = backgroundColor, shape = RoundedCornerShape(20.dp))
            .padding(16.dp)

        Row(
            modifier = buttonModifier,
            verticalAlignment = Alignment.CenterVertically
        ) {

            val textModifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)

            CircledImage(
                bitmap = avatarBitmap,
                modifier = Modifier.align(Alignment.CenterVertically),
                size = 64.dp
            )


            ContactText(name, isFromUser, message, time, textModifier)
        }
    }
}

@Composable
fun ContactsScreen(
    navController: NavHostController,
    viewModel: ContactsViewModel = viewModel(
        factory = ContactsViewModelFactory(LocalContext.current)
    ),
    currentScreen: MutableState<String>
) {

    val contacts by viewModel.contacts.collectAsState()
    val localUserId = PreferencesManager(LocalContext.current).getLocalUserId()

    LaunchedEffect(localUserId) {
        withContext(Dispatchers.Main) {
            viewModel.loadContacts(localUserId)
        }
    }

    ScreenTemplate(
        navIcon = {
            FooterWithPromptBar(currentScreen.value)
        },
        header = {
            Header(stringResource(R.string.your_contacts), modifier = Modifier.padding(bottom = 16.dp))
        },
        modifier = Modifier.swipeToNavigate(
            onSwipeUp =  {
                navController.navigate("settingsscreen")
            },
            onSwipeRight = {
                navController.navigate("availablescreen")
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
            val contactEntries = contacts.entries.toList()
            items(contactEntries.size) { index ->
                val (user, lastMessage) = contactEntries[index]
                UserCard(
                    avatarBitmap = user.avatar,
                    name = user.name,
                    isFromUser = lastMessage?.senderId == localUserId,
                    message = lastMessage?.content ?: "",
                    time = lastMessage?.timestamp ?: -1L,
                    backgroundColor = user.color,
                    onClick = { navController.navigate("talkscreen/${user.id}") }
                )
            }
        }
    }
}