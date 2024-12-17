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
import androidx.navigation.NavHostController
import com.example.telepathy.presentation.ui.ScreenTemplate
import com.example.telepathy.data.LocalPreferences

@Composable
fun PinScreenBase(
    headerText: String,
    pinLength: Int = 4,
    navController: NavHostController,
    onPinEntered: (String) -> Unit,
    onCancel: (() -> Unit)?,
    onPinUpdated: (String) -> Unit
) {
    var pin by remember { mutableStateOf("") }

    ScreenTemplate(
        navIcon = { },
        header = {
            Text(
                text = headerText,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        },
        modifier = Modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxSize()
        ) {
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

            // Keypad
            Keypad(
                pin = pin,
                onPinUpdated = { newPin ->
                    pin = newPin
                    onPinUpdated(newPin)
                },
                onPinEntered = onPinEntered,
                onCancel = onCancel
            )
        }
    }
}

@Composable
fun Keypad(
    pin: String,
    onPinEntered: (String) -> Unit,
    onCancel: (() -> Unit)?,
    onPinUpdated: (String) -> Unit
) {
    val keypadRows = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("Undo", "0", "OK")
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
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
                            fontSize = 24.sp,
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
            if (currentPin.isNotEmpty()) {
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

// Enter Current PIN Screen
@Composable
fun EnterPinScreen(
    navController: NavHostController,
    destinationRoute: String, // The destination route to navigate to
    onCancel: (() -> Unit)?
) {
    PinScreenBase(
        headerText = "Enter Current PIN",
        navController = navController,
        onPinEntered = { pin ->
            if (pin == LocalPreferences.PIN) {
                // Navigate to the provided destination route
                navController.navigate(destinationRoute)
            } else {
                // Show error message if PIN is incorrect
                // You can add a Snackbar or a Toast here to notify the user
            }
        },
        onCancel = onCancel,
        onPinUpdated = {}
    )
}


// Enter New PIN Screen
@Composable
fun EnterNewPinScreen(
    navController: NavHostController,
    onPinEntered: ((String) -> Unit)?,
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

// Confirm New PIN Screen
@Composable
fun ConfirmPinScreen(
    navController: NavHostController,
    onPinConfirmed: (String) -> Unit,
    onCancel: () -> Unit,
    pinTemp: String
) {
    var confirmPin by remember { mutableStateOf("") }

    PinScreenBase(
        headerText = "Confirm New PIN",
        navController = navController,
        onPinEntered = { confirmPin ->
            if (confirmPin == pinTemp) {
                LocalPreferences.PIN = confirmPin // Save the new PIN to LocalPreferences
                navController.navigate("home")  // Proceed to home or the next screen
            } else {
                // Show error message if PINs don't match
            }
        },
        onCancel = onCancel,
        onPinUpdated = { confirmPin = it }
    )
}
