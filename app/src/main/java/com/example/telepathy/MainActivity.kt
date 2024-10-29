package com.example.telepathy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
                text = stringResource(com.example.telepathy.R.string.your_contacts),
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
    Button (
        onClick = { /*TODO*/},
        shape = RoundedCornerShape(10.dp),

    ) {
        Row(

        ) {
            Avatar()
            ContactText()
        }
    }
}


@Composable
fun Avatar() {
}


@Composable
fun ContactText() {
    Text(
        text = "siema"
    )
}


@Preview(showBackground = true)
@Composable
fun TelePathyPreview() {
    TelePathyTheme {
        //ContactsScreen()
        Contact()
    }
}