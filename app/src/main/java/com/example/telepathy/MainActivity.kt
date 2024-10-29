package com.example.telepathy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Button
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
fun Contact() {
    val buttonModifier = Modifier
        .padding(horizontal = 40.dp)
        .fillMaxWidth()
        .size(140.dp)

    Button (
        onClick = { /*TODO*/},
        shape = RoundedCornerShape(20.dp),
        modifier = buttonModifier
    ) {
        Row (
            modifier = Modifier
                .fillMaxSize()
        ) {
            val avatarModifier = Modifier
                .align(Alignment.CenterVertically)

            val textModifier = Modifier
                .padding(16.dp)
            Avatar(painterResource(R.drawable.test), avatarModifier)
            ContactText(textModifier)
        }
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
fun ContactText(modifier: Modifier) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column {
            Text(
                text = "siema"
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TelePathyPreview() {
    TelePathyTheme {
        //ContactsScreen()
        Contact()
    }
}