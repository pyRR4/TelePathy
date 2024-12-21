package com.example.telepathy.presentation.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.telepathy.data.LocalPreferences
import com.example.telepathy.data.Message
import com.example.telepathy.data.User
import com.example.telepathy.presentation.ui.CircledImage
import com.example.telepathy.presentation.ui.DividerWithImage
import com.example.telepathy.presentation.ui.ScreenTemplate
import com.example.telepathy.presentation.navigation.swipeToNavigate


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
    backgroundColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(124.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(16.dp)),
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

    ScreenTemplate(
        navIcon = {
            DividerWithImage()
        },
        header = {
            TalkCard(
                avatarBitmap = user.avatar,
                name = user.name,
                description = user.description,
                backgroundColor = user.color
            )
        },
        modifier = Modifier.swipeToNavigate(
            isSwipeHandled = remember { mutableStateOf(false) },
            isNavigating = remember { mutableStateOf(false) },
            coroutineScope = rememberCoroutineScope(),
            onSwipeRight = { navController.navigate("mainscreens") }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(count = messages.size) { index ->
                    val message = messages[index]
                    val messageColor = if (message.fromLocalUser) Color(0xFF4CAF50) else user.color

                    MessageBubble(
                        message = message,
                        isLocalUser = message.fromLocalUser,
                        backgroundColor = messageColor
                    )
                }
            }
        }
    }
}
