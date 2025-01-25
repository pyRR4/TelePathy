package com.example.telepathy.presentation.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.telepathy.data.PreferencesManager
import com.example.telepathy.presentation.ui.CircledImage
import com.example.telepathy.data.entities.Message
import com.example.telepathy.presentation.navigation.swipeToNavigate
import com.example.telepathy.presentation.viewmodels.ChatViewModel
import com.example.telepathy.presentation.viewmodels.GenericViewModelFactory
import com.example.telepathy.presentation.viewmodels.SharedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun MessageBubble(
    message: Message,
    isLocalUser: Boolean,
    backgroundColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = if (isLocalUser) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp),
            horizontalAlignment = if (isLocalUser) Alignment.End else Alignment.Start
        ) {
            Text(
                text = message.content,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 16.sp,
                lineHeight = 22.sp
            )

            Text(
                text = formatTime(message.timestamp),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.padding(top = 4.dp),
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun TalkCard(
    avatarBitmap: Bitmap?,
    name: String,
    description: String,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(124.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(
                    topEnd = 16.dp,
                    bottomEnd = 16.dp
                )
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircledImage(
                bitmap = avatarBitmap,
                modifier = Modifier.align(Alignment.CenterVertically),
                size = 64.dp
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 1
                )

                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun TalkScreen(
    navController: NavHostController,
    viewModel: ChatViewModel = viewModel(
        factory = GenericViewModelFactory (LocalContext.current)
    ),
    remoteUserId: Int,
    previousScreen: MutableState<String>,
    sharedViewModel: SharedViewModel
) {
    val user by viewModel.currentUser.collectAsState()
    val messages by viewModel.chatHistory.collectAsState()

    var messageInput by remember { mutableStateOf("") }

    val localUser by sharedViewModel.localUser.collectAsState()

    val preferencesManager = PreferencesManager(LocalContext.current)
    val localUserId = preferencesManager.getLocalUserId()

    LaunchedEffect(localUser, remoteUserId) {
        withContext(Dispatchers.Main) {
            viewModel.loadUser(remoteUserId)
            viewModel.loadChatHistory(localUserId, remoteUserId)
        }
    }
    Column(
        modifier = Modifier.testTag("TalkScreen")
    ) {
        Text(text = "Talk")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .swipeToNavigate(
                onSwipeRight =  {
                    navController.navigate(previousScreen.value)
                },
                coroutineScope = rememberCoroutineScope(),
                isNavigating = remember { mutableStateOf(false) },
                isSwipeHandled = remember { mutableStateOf(false) }
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .height(124.dp)
                    .width(32.dp)
                    .background(
                        color = user?.color ?: MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            bottomStart = 16.dp
                        )
                    ),
                contentAlignment = Alignment.Center,
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            TalkCard(
                avatarBitmap = user?.avatar,
                name = user?.name ?: "",
                description = user?.description ?: "",
                backgroundColor = user?.color ?: MaterialTheme.colorScheme.surface,
                onClick = { /* */ }
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(count = messages.size) { index ->
                val message = messages[index]
                val messageColor = if (message.senderId == localUser?.id) localUser?.color
                else user?.color ?: MaterialTheme.colorScheme.surface

                MessageBubble(
                    message = message,
                    isLocalUser = message.senderId == localUser?.id,
                    backgroundColor = messageColor ?: MaterialTheme.colorScheme.surface
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* */ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            TextField(
                value = messageInput,
                onValueChange = { messageInput = it },
                placeholder = { Text("Type a message...") },
                modifier = Modifier.weight(1f),
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if(localUser != null) {
                        viewModel.sendMessage(messageInput, localUser!!.id, remoteUserId)
                    }
                    messageInput = ""
                },
                modifier = Modifier.height(48.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}