package com.example.telepathy.presentation.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.telepathy.data.LocalPreferences
import com.example.telepathy.data.LocalPreferences.localUser
import com.example.telepathy.data.Message
import com.example.telepathy.data.User
import com.example.telepathy.presentation.ui.CircledImage

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
                color = Color.White,
                fontSize = 16.sp,
                lineHeight = 22.sp
            )

            Text(
                text = formatTime(message.timestamp),
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.6f),
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
            .background(color = backgroundColor, shape = RoundedCornerShape(16.dp))
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
                    color = Color.White,
                    maxLines = 1
                )

                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun TalkScreen(navController: NavHostController, user: User) {
    val messages = user.chatHistory
    var messageInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
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
                    .width(36.dp)
                    .background(color = Color.Black, shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center // Center the icon inside the box
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }

            TalkCard(
                avatarBitmap = user.avatar,
                name = user.name,
                description = user.description,
                backgroundColor = user.color,
                onClick = { /* Handle banner click */ }
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
                val messageColor = if (message.fromLocalUser) localUser!!.color  else user.color

                MessageBubble(
                    message = message,
                    isLocalUser = message.fromLocalUser,
                    backgroundColor = messageColor
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Handle more button */ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More",
                    tint = Color.White
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
                onClick = { /* Handle send action */ },
                modifier = Modifier.height(48.dp)
            ) {
                Text(text = "Send")
            }
        }
    }
}