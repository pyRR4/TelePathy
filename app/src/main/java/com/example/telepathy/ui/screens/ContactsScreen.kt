package com.example.telepathy.ui.screens

import android.graphics.Bitmap
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import com.example.telepathy.clases.User
import com.example.telepathy.ui.CicrcledImage
import com.example.telepathy.ui.DividerWithImage


@Composable
fun Header(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 48.sp,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}


@Composable
fun Avatar(image: Painter, modifier: Modifier) {
    CicrcledImage(image, modifier)
}

@Composable
fun ContactText(name: String, isFromUser: Boolean, message: String, time: String, modifier: Modifier) {
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
            text = time,
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
    time: String,
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
        val avatarModifier = Modifier
            .align(Alignment.CenterVertically)
            .size(64.dp)
            .clip(CircleShape)

        val textModifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)

        // Wyświetl awatar z Bitmap lub obraz domyślny
        if (avatarBitmap != null) {
            Image(
                bitmap = avatarBitmap.asImageBitmap(),
                contentDescription = "Avatar",
                modifier = avatarModifier,
                contentScale = ContentScale.Crop
            )
        } else {
            // Domyślny obraz, jeśli `avatarBitmap` to null
            Image(
                painter = painterResource(R.drawable.test),
                contentDescription = "Default Avatar",
                modifier = avatarModifier,
                contentScale = ContentScale.Crop
            )
        }

        ContactText(name, isFromUser, message, time, textModifier)
    }
}

@Composable
fun ContactsScreen(navController: NavHostController, users: List<User>) {
    var isSwipeHandled by remember { mutableStateOf(false) }
    var isNavigating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Handle back press manually
    BackHandler {
        if (!isSwipeHandled && !isNavigating) {
            Log.d("BackPress", "ContactsScreen - Back Pressed")
            coroutineScope.launch {
                delay(200)  // Delay to ensure swipe completion
                navController.popBackStack()  // Go back to Main Screen
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    // Log swipe gestures and the swipe amount
                    Log.d("SwipeGesture", "ContactsScreen - Swipe Amount (X, Y): (${dragAmount.x}, ${dragAmount.y})")

                    if (!isSwipeHandled && !isNavigating) {
                        // Horizontal swipe detection
                        if (dragAmount.x < -100f) {  // Swipe left to go back
                            Log.d("SwipeGesture", "ContactsScreen - Horizontal Swipe: Left")
                            isNavigating = true
                            coroutineScope.launch {
                                delay(200)  // Delay to ensure swipe completion
                                navController.popBackStack()
                                isSwipeHandled = true  // Mark swipe as handled
                                isNavigating = false
                            }
                        }
                    }
                }
            }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header(
                text = stringResource(R.string.your_contacts),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            val aspectRatio = 570f / 140f // Oryginalna proporcja
            val height = (LocalConfiguration.current.screenWidthDp.dp / aspectRatio)
            val width = LocalConfiguration.current.screenWidthDp.dp

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .size(width, height),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                items(count = users.size) { index ->
                    val user = users[index]
                    UserCard(
                        avatarBitmap = user.avatar,
                        name = user.name,
                        isFromUser = user.isLocalUser,
                        message = user.chatHistory.firstOrNull()?.content ?: "Brak wiadomości",
                        time = user.chatHistory.firstOrNull()?.timestamp?.toString() ?: "Brak czasu",
                        backgroundColor = user.color,
                        onClick = { /* Handle click for this user */ }
                    )
                }
            }

            DividerWithImage()
        }
    }
}
