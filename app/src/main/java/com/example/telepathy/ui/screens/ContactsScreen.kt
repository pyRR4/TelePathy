package com.example.telepathy.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telepathy.R
import androidx.compose.ui.res.stringResource
import com.example.telepathy.ui.CicrcledImage
import com.example.telepathy.ui.DividerWithImage
import com.example.telepathy.ui.ScreenTemplate
import com.example.telepathy.ui.swipeToNavigate

@Composable
fun Avatar(image: Painter, modifier: Modifier) {
    CicrcledImage(image, modifier)
}

@Composable
fun ContactText(name: String, isFromUser: Boolean, message: String, time: String, modifier: Modifier) {
    var msg = message;
    msg = if (isFromUser) {
        "Ty:\n$msg"
    } else {
        "$name:\n$msg"
    }
    Row (
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column (
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

        Text (
            text = time,
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.8f),
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun ContactCard(
    imageDrawable: Int,
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

    Row (
        modifier = buttonModifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val avatarModifier = Modifier
            .align(Alignment.CenterVertically)

        val textModifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
        Avatar(painterResource(imageDrawable), avatarModifier)
        ContactText(name, isFromUser, message, time, textModifier)
    }
}


data class Contact(
    val imageDrawable: Int,
    val name: String,
    val isFromUser: Boolean,
    val message: String,
    val time: String,
    val backgroundColor: Color
)

@Composable
fun ContactsScreen(navController: NavHostController, contacts: List<Contact>) {
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
            items(count = contacts.size) { index ->
                val contact = contacts[index]
                ContactCard(
                    imageDrawable = contact.imageDrawable,
                    name = contact.name,
                    isFromUser = contact.isFromUser,
                    message = contact.message,
                    time = contact.time,
                    backgroundColor = contact.backgroundColor,
                    onClick = { /* Handle click */ }
                )
            }
        }
    }
}

