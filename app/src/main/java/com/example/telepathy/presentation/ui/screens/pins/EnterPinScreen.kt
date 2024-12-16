package com.example.telepathy.presentation.ui.screens.pins

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.telepathy.R
import com.example.telepathy.presentation.ui.DividerWithImage
import com.example.telepathy.presentation.ui.Header
import com.example.telepathy.presentation.ui.ScreenTemplate
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.res.stringResource


@Composable
fun EnterPinScreen(navController: NavHostController) {
    var pin by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    ScreenTemplate(
        navIcon = {
            DividerWithImage()
        },
        header = {
            Header(stringResource(R.string.enter_pin), modifier = Modifier.padding(bottom = 16.dp))
        },
        modifier = Modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = pin,
                onValueChange = {
                    if (it.length <= 4) pin = it
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = showError,
                modifier = Modifier.fillMaxWidth()
            )

            if (showError) {
                Text(
                    text = stringResource(R.string.wrong_pin),
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Button(
                onClick = {
                    if (pin.length == 4) {
                        showError = false
                        // Navigate to the next screen or verify PIN
                        navController.navigate("next_screen")
                    } else {
                        showError = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.ok), fontSize = 16.sp, color = Color.White)
            }
        }
    }
}
