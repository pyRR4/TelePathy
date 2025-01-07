package com.example.telepathy.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import com.example.telepathy.presentation.ui.DividerWithImage
import androidx.compose.ui.platform.LocalContext


@Composable
fun PinScreenBase(
    headerText: String,
    pinLength: Int = 4,
    navController: NavHostController,
    onPinEntered: (String) -> Unit,
    onCancel: (() -> Unit)?,
    onPinUpdated: (String) -> Unit,
    keypadWidth: Dp = 300.dp, // Keypad width as a parameter
    keypadHeight: Dp = 400.dp // Keypad height as a parameter
) {
    var pin by remember { mutableStateOf("") }

    ScreenTemplate(
        navIcon = { DividerWithImage() },
        header = null,
        modifier = Modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Logo box at the top (currently empty, filling max width)
            Box(
                modifier = Modifier
                    .fillMaxWidth() // Logo box fills max width
                    .height(120.dp) // Example height for the logo box
                    .background(Color.Gray) // Placeholder color for the logo box
            )
            Spacer(Modifier.height(60.dp))

            // Display header text above dots
            Text(
                text = headerText,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Dots to represent PIN
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                repeat(pinLength) { index ->
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(
                                color = if (index < pin.length) Color.Black else Color.Gray,
                                shape = CircleShape
                            )
                    )
                }
            }

            // Keypad under the logo box
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
    keypadWidth: Dp = 250.dp, // Keypad width as a parameter
    keypadHeight: Dp = 250.dp // Keypad height as a parameter
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
            .width(keypadWidth) // Set the width of the keypad
            .height(keypadHeight) // Set the height of the keypad
            .padding(16.dp)
    ) {
        keypadRows.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                row.forEach { key ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(Color.LightGray, shape = CircleShape)
                            .clickable {
                                handleKeyInput(key, pin, onPinEntered, onCancel) { newPin ->
                                    onPinUpdated(newPin)
                                }
                            }
                    ) {
                        Text(
                            text = key,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(8.dp)
                        )
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
    onCancel: (() -> Unit)?,
    onPinUpdated: (String) -> Unit
) {
    when (key) {
        "Undo" -> {
            if (currentPin.isEmpty()) {
                onCancel?.invoke() // Navigate back if pin is empty
            } else {
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
                .background(messageColor, shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
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
        onDismiss() // Call onDismiss after the delay
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

    PinScreenBase(
        headerText = "Enter Current PIN",
        navController = navController,
        onPinEntered = { enteredPin ->
            if (enteredPin == PIN) {
                navController.navigate(destinationRoute)
            } else {
                errorMessage = "Incorrect PIN"
                pin = "" // Reset PIN
            }
        },
        onCancel = onCancel,
        onPinUpdated = { updatedPin ->
            errorMessage = null // Clear error when PIN is updated
            pin = updatedPin
        }
    )

    // Show error message if PIN is incorrect
    errorMessage?.let {
        MessageDialog(
            message = it,
            messageColor = Color.Red,
            onDismiss = { errorMessage = null } // Reset error message after dismiss
        )
    }
}

@Composable
fun EnterNewPinScreen(
    navController: NavHostController,
    onCancel: (() -> Unit)?
) {
    var pinTemp by remember { mutableStateOf("") }

    PinScreenBase(
        headerText = "Enter New PIN",
        navController = navController,
        onPinEntered = { pin ->
            pinTemp = pin
            navController.navigate("confirm_new_pin/$pinTemp") // Pass the pinTemp to confirm screen
        },
        onCancel = onCancel,
        onPinUpdated = { pinTemp = it }
    )
}

@Composable
fun ConfirmPinScreen(
    navController: NavHostController,
    onCancel: () -> Unit, // Cancel callback
    pinTemp: String // Temporary PIN to compare with
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
                messageColor = Color(0xFF4CAF50) // Green for success
                preferencesManager.savePin(enteredPin)
                pinsMatch = true

            } else {
                message = "PINs do not match. Try again."
                messageColor = Color(0xFFF44336) // Red for error
                pinsMatch = false
            }
        },
        onCancel = onCancel,
        onPinUpdated = { confirmPin = it }
    )

    // Show success or error message
    message?.let {
        MessageDialog(
            message = it,
            messageColor = messageColor,
            onDismiss = { message = null } // Reset message after dismiss
        )
    }

    LaunchedEffect(pinsMatch) {
        if (pinsMatch) {
            kotlinx.coroutines.delay(2000) // Allow the success message to be visible for 2 seconds
            navController.navigate("mainscreens")
        } else if (messageColor == Color(0xFFF44336)) {
            kotlinx.coroutines.delay(2000) // Allow the error message to be visible for 2 seconds
            navController.popBackStack()
        }
    }
}
