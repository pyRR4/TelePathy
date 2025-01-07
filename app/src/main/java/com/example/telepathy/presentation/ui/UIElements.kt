package com.example.telepathy.presentation.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telepathy.R
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext


@Composable
fun CircledImage(
    bitmap: Bitmap? = null, // Optional bitmap
    modifier: Modifier = Modifier,
    size: Dp = 90.dp,
    defaultColor: Color = Color.Black // Kolor kółka, gdy bitmapa jest null
) {
    val avatarModifier = modifier
        .size(size)
        .clip(CircleShape)

    if (bitmap != null) {
        // Wyświetlanie obrazka (gdy bitmapa jest dostępna)
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Avatar",
            modifier = avatarModifier,
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = avatarModifier
                .background(defaultColor)
        )
    }
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
            .fillMaxWidth()
            .height(84.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        val buttonModifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onClick)
            .background(color = backgroundColor, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)

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
        HorizontalDivider(
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
    val res = LocalContext.current.resources
    val bitmap = remember(res) {
        BitmapFactory.decodeResource(res, R.drawable.test1)
    }

    Image(
        painter = BitmapPainter(bitmap.asImageBitmap()),
        contentDescription = null,
        modifier = Modifier.size(80.dp)
    )
}

@Composable
fun ScreenTemplate(
    navIcon: @Composable (() -> Unit),
    header: @Composable (() -> Unit)?,
    modifier: Modifier,
    content: @Composable (ColumnScope.() -> Unit)
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
            header?.invoke()

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