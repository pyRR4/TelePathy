package com.example.telepathy.ui.screens

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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
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

    Image (
        painter = image,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .requiredSize(90.dp)
            .clip(CircleShape)
    )

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

                items(count = contacts.size) {
                    index ->
                    val contact = contacts[index]
                    ContactCard(
                        imageDrawable = contact.imageDrawable,
                        name = contact.name,
                        isFromUser = contact.isFromUser,
                        message = contact.message,
                        time = contact.time,
                        backgroundColor = contact.backgroundColor,
                        onClick = { /* Handle click for this contact */ }
                    )
                }
            }

            DividerWithImage()
        }
    }
}

@Composable
fun DividerWithImage() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Divider(
            color = Color.LightGray,
            thickness = 2.dp,
            modifier = Modifier
                .alpha((0.6).toFloat())
                .padding(vertical = 16.dp)
                .width(LocalConfiguration.current.screenWidthDp.dp / 2)
        )
        BottomImage()
    }
}

@Composable
fun BottomImage() {
    Image(
        painter = painterResource(R.drawable.test),
        contentDescription = null,
        modifier = Modifier.size(80.dp)
    )
}