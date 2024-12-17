package com.example.telepathy.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.telepathy.R
import com.example.telepathy.presentation.ui.Header
import com.example.telepathy.data.LocalPreferences.localUser
import com.example.telepathy.presentation.ui.ScreenTemplate


@Composable
fun ChangeNameScreen(navController: NavHostController) {
    var text by remember { mutableStateOf(localUser!!.name) }
    val charLimit = 15
    var charactersLeft by remember { mutableIntStateOf(charLimit - text.length) }

    ChangeTextScreenContent(
        header = stringResource(R.string.change_name),
        text = text,
        charLimit = charLimit,
        onTextChange = {
            if (it.length <= charLimit) {
                text = it
                charactersLeft = charLimit - it.length
            }
        },
        charactersLeft = charactersLeft,
        onSave = {
            localUser!!.name = text
            navController.popBackStack()
        },
        onCancel = {
            navController.popBackStack()
        }
    )
}

@Composable
fun ChangeDescriptionScreen(navController: NavHostController) {
    var text by remember { mutableStateOf(localUser!!.description) }
    val charLimit = 30
    var charactersLeft by remember { mutableIntStateOf(charLimit - text.length) }

    ChangeTextScreenContent(
        header = stringResource(R.string.change_desc),
        text = text,
        charLimit = charLimit,
        onTextChange = {
            if (it.length <= charLimit) {
                text = it
                charactersLeft = charLimit - it.length
            }
        },
        charactersLeft = charactersLeft,
        onSave = {
            localUser!!.description = text
            navController.popBackStack()
        },
        onCancel = {
            navController.popBackStack()
        }
    )
}

@Composable
private fun ChangeTextScreenContent(
    header: String,
    text: String,
    charLimit: Int,
    onTextChange: (String) -> Unit,
    charactersLeft: Int,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    ScreenTemplate(
        navIcon = {},
        header = {
            Header(text = header, modifier = Modifier.padding(bottom = 16.dp))
        },
        modifier = Modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = text,
                    onValueChange = onTextChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    textStyle = TextStyle(fontSize = 16.sp),
                    placeholder = {
                        BasicText("Enter your text here...", style = TextStyle(color = androidx.compose.ui.graphics.Color.Gray))
                    }
                )
                BasicText(
                    text = "$charactersLeft",
                    style = TextStyle(fontSize = 12.sp, color = androidx.compose.ui.graphics.Color.Gray),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = onCancel) {
                    BasicText(text = "Cancel", style = TextStyle(fontSize = 16.sp))
                }
                Button(onClick = onSave) {
                    BasicText(text = "Save", style = TextStyle(fontSize = 16.sp))
                }
            }
        }
    }
}
