package com.example.telepathy.ui.screens

import android.graphics.Bitmap
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telepathy.R
import androidx.compose.ui.res.stringResource
import com.example.telepathy.clases.User
import com.example.telepathy.ui.CircledImage
import com.example.telepathy.ui.DividerWithImage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.telepathy.ui.ScreenTemplate
import com.example.telepathy.ui.utils.swipeToNavigate

@Composable
fun formatTime(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val date = Date(timestamp)
    return dateFormat.format(date)
}

@Composable
fun ContactText(name: String, isFromUser: Boolean, message: String, timestamp: Long, modifier: Modifier) {
    val formattedTime = formatTime(timestamp)  // Formatujemy czas do HH:MM
    var msg = message
    msg = if (isFromUser) {
        "Ty:\n$msg"
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
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = msg,
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.8f),
                lineHeight = 18.sp,
                modifier = Modifier.padding(top = 10.dp),
                overflow = TextOverflow.Ellipsis
            )
        }

        Text(
            text = formattedTime,
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.8f),
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
    val buttonModifier = Modifier
        .size(height = 140.dp, width = 570.dp)
        .clickable(onClick = onClick)
        .padding(horizontal = 16.dp, vertical = 8.dp)
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

@Composable
fun ContactsScreen(navController: NavHostController, users: List<User>) {
    var isSwipeHandled by remember { mutableStateOf(false) }
    var isNavigating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    ScreenTemplate(
        title = stringResource(R.string.your_contacts),
        navIcon = { DividerWithImage() },
        modifier = Modifier.swipeToNavigate(
            isSwipeHandled = remember { mutableStateOf(isSwipeHandled) },
            isNavigating = remember { mutableStateOf(isNavigating) },
            coroutineScope = coroutineScope,
            onSwipeRight = { navController.navigate("available") },
            onSwipeDown = { navController.navigate("settings") },
        )
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(count = users.size) { index ->
                val user = users[index]
                UserCard(
                    avatarBitmap = user.avatar,
                    name = user.name,
                    isFromUser = user.chatHistory.lastOrNull()?.fromLocalUser == true,
                    message = user.chatHistory.lastOrNull()?.content ?: "Brak wiadomości",
                    time = user.chatHistory.lastOrNull()?.timestamp ?: 0L,
                    backgroundColor = user.color,
                    onClick = { /* Handle click for this user */ }
                )
            }
        }
    }
}
