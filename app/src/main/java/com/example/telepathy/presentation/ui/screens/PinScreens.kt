package com.example.telepathy.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavHostController
import com.example.telepathy.data.PreferencesManager
import com.example.telepathy.presentation.ui.ScreenTemplate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import com.example.telepathy.presentation.ui.theme.DarkButtonsColor
import com.example.telepathy.R
import com.example.telepathy.presentation.ui.StaticFooter
import com.example.telepathy.presentation.ui.TelePathyLogo

@Composable
fun PinScreenBase(
    headerText: String,
    pinLength: Int = 4,
    navController: NavHostController,
    onPinEntered: (String) -> Unit,
    onCancel: (() -> Unit)?,
    onPinUpdated: (String) -> Unit,
    keypadWidth: Dp = 300.dp,
    keypadHeight: Dp = 400.dp
) {
    var pin by remember { mutableStateOf("") }

    ScreenTemplate(
        navIcon = { StaticFooter() },
        header = { TelePathyLogo() },
        modifier = Modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(Modifier.height(60.dp))

            Text(
                text = headerText,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                repeat(pinLength) { index ->
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(
                                color = if (index < pin.length) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onPrimary,
                                shape = CircleShape
                            )
                    )
                }
            }

            Keypad(
                pin = pin,
                onPinUpdated = { newPin ->
                    pin = newPin
                    onPinUpdated(newPin)
                },
                onPinEntered = onPinEntered,
                onCancel = onCancel,
                keypadWidth = keypadWidth,
                keypadHeight = keypadHeight
            )
        }
    }
}

@Composable
fun Keypad(
    pin: String,
    onPinEntered: (String) -> Unit,
    onCancel: (() -> Unit)?,
    onPinUpdated: (String) -> Unit,
    keypadWidth: Dp = 250.dp,
    keypadHeight: Dp = 250.dp
) {
    val keypadRows = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("Undo", "0", "OK")
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .width(keypadWidth)
            .height(keypadHeight)
            .padding(16.dp)
    ) {
        keypadRows.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                row.forEach { key ->
                    Button(
                        colors = ButtonColors(
                            contentColor = DarkButtonsColor,
                            disabledContentColor = DarkButtonsColor,
                            disabledContainerColor = DarkButtonsColor,
                            containerColor = DarkButtonsColor
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .align(Alignment.CenterVertically),
                        onClick = { handleKeyInput(key, pin, onPinEntered) { newPin ->
                            onPinUpdated(newPin)
                        }},
                        shape = CircleShape
                    ) {
                        when(key) {
                            "Undo" -> {
                                Icon(
                                    painter = painterResource(R.drawable.delete),
                                    contentDescription = "Back",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            "OK" -> {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = "Back",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            else -> {
                                Text(
                                    text = key,
                                    fontSize = 28.sp,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun handleKeyInput(
    key: String,
    currentPin: String,
    onPinEntered: (String) -> Unit,
    onPinUpdated: (String) -> Unit
) {
    when (key) {
        "Undo" -> {
            if (!currentPin.isEmpty()) {
                onPinUpdated(currentPin.dropLast(1))
            }
        }
        "OK" -> {
            if (currentPin.length == 4) {
                onPinEntered(currentPin)
            }
        }
        else -> {
            if (currentPin.length < 4 && key.all { it.isDigit() }) {
                onPinUpdated(currentPin + key)
            }
        }
    }
}

@Composable
fun MessageDialog(
    message: String,
    messageColor: Color,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .background(
                    messageColor,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 24.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    LaunchedEffect(true) {
        kotlinx.coroutines.delay(2000)
        onDismiss()
    }
}

@Composable
fun EnterPinScreen(
    navController: NavHostController,
    destinationRoute: String,
    onCancel: (() -> Unit)?
) {
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var pin by remember { mutableStateOf("") }

    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)
    val PIN = preferencesManager.getPin()

    LaunchedEffect(PIN) {
        if (PIN == null ) {
            navController.navigate(destinationRoute)
        }
    }

    PinScreenBase(
        headerText = "Enter Current PIN",
        navController = navController,
        onPinEntered = { enteredPin ->
            if (enteredPin == PIN) {
                navController.navigate(destinationRoute)
            } else {
                errorMessage = "Incorrect PIN"
                pin = ""
            }
        },
        onCancel = onCancel,
        onPinUpdated = { updatedPin ->
            errorMessage = null
            pin = updatedPin
        }
    )

    errorMessage?.let {
        MessageDialog(
            message = it,
            messageColor = Color.Red,
            onDismiss = { errorMessage = null }
        )
    }
}


@Composable
fun EnterNewPinScreen(
    navController: NavHostController,
    onCancel: (() -> Unit)?
) {
    var pinTemp by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.testTag("PinScreen")
    ) {
        Text(text = "Pin")
    }

    PinScreenBase(
        headerText = "Set New PIN",
        navController = navController,
        onPinEntered = { pin ->
            pinTemp = pin
            navController.navigate("confirm_new_pin/$pinTemp")
        },
        onCancel = onCancel,
        onPinUpdated = { pinTemp = it }
    )
}

@Composable
fun ConfirmPinScreen(
    navController: NavHostController,
    onCancel: () -> Unit,
    pinTemp: String
) {
    var confirmPin by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }
    var messageColor by remember { mutableStateOf(Color.Transparent) }
    var pinsMatch by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)

    PinScreenBase(
        headerText = "Confirm New PIN",
        navController = navController,
        onPinEntered = { enteredPin ->
            if (enteredPin == pinTemp) {
                message = "PINs match! PIN updated successfully."
                messageColor = Color(0xFF4CAF50)
                preferencesManager.savePin(enteredPin)
                pinsMatch = true

            } else {
                message = "PINs do not match. Try again."
                messageColor = Color(0xFFF44336)
                pinsMatch = false
            }
        },
        onCancel = onCancel,
        onPinUpdated = { confirmPin = it }
    )

    message?.let {
        MessageDialog(
            message = it,
            messageColor = messageColor,
            onDismiss = { message = null }
        )
    }

    LaunchedEffect(pinsMatch) {
        if (pinsMatch) {
            kotlinx.coroutines.delay(2000)
            navController.navigate("contactsscreen")
        } else if (messageColor == Color(0xFFF44336)) {
            kotlinx.coroutines.delay(2000)
            navController.popBackStack()
        }
    }
}
