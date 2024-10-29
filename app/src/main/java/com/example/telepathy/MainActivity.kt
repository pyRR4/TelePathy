package com.example.telepathy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telepathy.ui.theme.TelePathyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TelePathyTheme {
                ContactsScreen()
            }
        }
    }
}

@Composable
fun ContactsScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column (
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .background(color = Color.DarkGray)
                .padding(top = 40.dp)
                .fillMaxWidth()
        ) {
            Header(
                text = stringResource(R.string.your_contacts),
                modifier = Modifier
                    .align(
                        Alignment.CenterHorizontally
                    )
            )

            Column (
                modifier = Modifier
            ) {

            }
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
fun ContactCard(
    imageDrawable: Int,
    name: String,
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
        ContactText(name, message, time, textModifier)
    }
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
fun ContactText(name: String, message: String, time: String, modifier: Modifier) {
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
                color = Color.White
            )

            Text(
                text = message,
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.8f),
                lineHeight = 18.sp,
                modifier = Modifier.padding(top = 10.dp)
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


@Preview(showBackground = true)
@Composable
fun TelePathyPreview() {
    TelePathyTheme {
        //ContactsScreen()
        ContactCard(
            R.drawable.test,
            "AmatorUczciwiec000",
            "Ty:\nsiema",
            "15:37",
            Color.Blue,
            { println("Button clicked!") }
        )
    }
}