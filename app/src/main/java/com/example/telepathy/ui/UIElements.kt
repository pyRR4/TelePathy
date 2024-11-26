package com.example.telepathy.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.telepathy.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.launch

fun Modifier.swipeToNavigate(
    coroutineScope: CoroutineScope,
    onSwipeLeft: (() -> Unit)? = null,
    onSwipeRight: (() -> Unit)? = null,
    onSwipeUp: (() -> Unit)? = null,
    onSwipeDown: (() -> Unit)? = null,
    isSwipeHandled: MutableState<Boolean>,
    isNavigating: MutableState<Boolean>
): Modifier = this.pointerInput(Unit) {
    detectDragGestures { change, dragAmount ->
        change.consume()


        if (!isSwipeHandled.value && !isNavigating.value) {
            Log.d("SwipeGesture", "Drag Amount: x = ${dragAmount.x}, y = ${dragAmount.y}")
            when {
                dragAmount.x < -100f && onSwipeLeft != null -> {
                    Log.d("SwipeGesture", "Swipe Left detected")
                    isNavigating.value = true
                    coroutineScope.launch {
                        onSwipeLeft()
                        delay(200)
                        isSwipeHandled.value = true
                        isNavigating.value = false
                    }
                }
                dragAmount.y < -100f && onSwipeDown != null -> {
                    Log.d("SwipeGesture", "Swipe Down detected")
                    isNavigating.value = true
                    coroutineScope.launch {
                        onSwipeDown()
                        delay(200)
                        isSwipeHandled.value = true
                        isNavigating.value = false
                    }
                }
                dragAmount.x > 100f && onSwipeRight != null -> {
                    Log.d("SwipeGesture", "Swipe Right detected")
                    isNavigating.value = true
                    coroutineScope.launch {
                        onSwipeRight()
                        delay(200)
                        isSwipeHandled.value = true
                        isNavigating.value = false
                    }
                }
                dragAmount.y > 100f && onSwipeUp != null -> {
                    Log.d("SwipeGesture", "Swipe Up detected")
                    isNavigating.value = true
                    coroutineScope.launch {
                        onSwipeUp()
                        delay(200)
                        isSwipeHandled.value = true
                        isNavigating.value = false
                    }
                }
            }
        }
    }
}



@Composable
fun CicrcledImage(
    image: Painter,
    modifier: Modifier = Modifier,
    size: Dp = 90.dp
) {
    Image(
        painter = image,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .requiredSize(size)
            .clip(CircleShape)
    )
}


@Composable
fun CustomButton(
    name: String,
    backgroundColor: Color,
    image: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(570.dp)
            .height(80.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        val buttonModifier = Modifier
            .size(height = 140.dp, width = 570.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(20.dp))

        Row(
            modifier = buttonModifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            image()

            Text(
                text = name,
                color = Color.White,
                fontSize = 25.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

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
        painter = painterResource(R.drawable.test1),
        contentDescription = null,
        modifier = Modifier.size(80.dp)
    )
}

@Composable
fun ScreenTemplate(
    title: String,
    navIcon: @Composable () -> Unit,
    modifier: Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.DarkGray
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header(
                text = title,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Top
            ) {
                content()
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                navIcon()
            }
        }
    }
}
