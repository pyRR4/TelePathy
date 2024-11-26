package com.example.telepathy.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telepathy.R

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
            .height(100.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(8.dp)),
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