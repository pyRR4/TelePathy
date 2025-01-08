package com.example.telepathy.presentation.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.telepathy.presentation.ui.theme.DarkPurple


@Composable
fun CircledImage(
    bitmap: Bitmap? = null, // Optional bitmap
    modifier: Modifier = Modifier,
    size: Dp = 90.dp,
    defaultColor: Color = MaterialTheme.colorScheme.secondary // Kolor kółka, gdy bitmapa jest null
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
                color = MaterialTheme.colorScheme.onPrimary,
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
        color = MaterialTheme.colorScheme.onPrimary,
        fontSize = 48.sp,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Composable
fun FooterWithPromptBar(
    currentScreen: String
) {
    DividerTemplate(
        Modifier
    ) {
        PromptBar(
            currentScreen
        )
    }
}

@Composable
fun StaticFooter() {
    DividerTemplate(
        Modifier
    ) {
        TelePathyLogo()
    }
}


@Composable
fun DividerTemplate(
    modifier: Modifier,
    footer: @Composable (() -> Unit)
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.secondary,
            thickness = 2.dp,
            modifier = modifier
                .alpha((0.6).toFloat())
                .padding(vertical = 16.dp)
                .width(LocalConfiguration.current.screenWidthDp.dp / 2)
        )

        footer()
    }
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
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp, 16.dp, 16.dp, 0.dp),
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


@Composable
fun TelePathyLogo() {
    val gradientBrush = Brush.horizontalGradient(
        listOf(MaterialTheme.colorScheme.onSecondary, DarkPurple) // You can customize the gradient colors
    )
    val gradientBrush1 = Brush.horizontalGradient(
        listOf(DarkPurple, MaterialTheme.colorScheme.onSecondary) // You can customize the gradient colors
    )

    Row(
        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp)
    ) {
        Text(
            text = "Tele",
            style = TextStyle(
                fontSize = 35.sp,
                fontWeight = FontWeight.ExtraBold,
                brush = gradientBrush1,
                drawStyle = Stroke(  // Outline settings
                    width = 4f,       // Outline width
                    miter = 10f,      // Miter join for sharp corners
                    join = StrokeJoin.Round  // Rounded corners for the outline
                )
            ),
            textAlign = TextAlign.Start
        )

        Text(
            text = "Pathy",
            style = TextStyle(
                fontSize = 35.sp,
                fontWeight = FontWeight.ExtraBold,
                fontStyle = FontStyle.Italic,
                brush = gradientBrush,
                drawStyle = Stroke(  // Outline settings
                    width = 4f,       // Outline width
                    miter = 10f,      // Miter join for sharp corners
                    join = StrokeJoin.Round,  // Rounded corners for the outline
                )
            ),
            textAlign = TextAlign.End
        )
    }
}


@Composable
fun PromptBar(
    currentScreen: String
) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LeftIcon(currentScreen)

            TelePathyLogo()

            RightIcon(currentScreen)
        }
        if(currentScreen == "settingsscreen")
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Search",
                modifier = Modifier.size(24.dp)
            )
        else
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Search",
                modifier = Modifier.size(24.dp)
            )
    }
}

@Composable
fun PlaceholderIcon() {
    Spacer(modifier = Modifier.size(48.dp))
}


@Composable
fun LeftIcon(currentScreen: String) {
    Row(
        horizontalArrangement = Arrangement.Absolute.Left,
        verticalAlignment = Alignment.CenterVertically
    ) {
        when(currentScreen){
            "settingsscreen" -> PlaceholderIcon()
            "contactsscreen" ->{
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Arrow Left",
                    modifier = Modifier.size(24.dp)
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Search",
                    modifier = Modifier.size(24.dp)
                )
            }
            "availablescreen" -> PlaceholderIcon()
        }
    }
}

@Composable
fun RightIcon(currentScreen: String) {
    Row(
        horizontalArrangement = Arrangement.Absolute.Right,
        verticalAlignment = Alignment.CenterVertically
    ) {
        when(currentScreen) {
            "settingsscreen" -> PlaceholderIcon()
            "contactsscreen" -> {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Arrow Left",
                    modifier = Modifier.size(24.dp)
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Arrow Right",
                    modifier = Modifier.size(24.dp)
                )
            }

            "availablescreen" -> {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.List,
                    contentDescription = "Arrow Left",
                    modifier = Modifier.size(24.dp)
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Arrow Right",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}