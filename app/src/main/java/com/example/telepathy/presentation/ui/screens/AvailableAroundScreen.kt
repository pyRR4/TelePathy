package com.example.telepathy.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.telepathy.R
import com.example.telepathy.presentation.ui.CustomButton
import com.example.telepathy.presentation.ui.Header
import com.example.telepathy.presentation.ui.ScreenTemplate
import com.example.telepathy.presentation.viewmodels.AvailableViewModel
import com.example.telepathy.presentation.navigation.swipeToNavigate
import com.example.telepathy.presentation.ui.CircledImage
import com.example.telepathy.presentation.viewmodels.GenericViewModelFactory
import com.example.telepathy.presentation.viewmodels.SharedViewModel

@Composable
fun AvailableAroundScreen(
    navController: NavHostController,
    viewModel: AvailableViewModel = viewModel(
        factory = GenericViewModelFactory (LocalContext.current)
    ),
    currentScreen: MutableState<String>,
    sharedViewModel: SharedViewModel
) {
    var isVisible = viewModel.isDiscoverable.collectAsState()
    val discoveredUsers by viewModel.discoveredUsers.collectAsState()
    val filteredDiscoveredUsers by viewModel.filteredUsers.collectAsState()
    val localUser by sharedViewModel.localUser.collectAsState()

    var showSuccessDialog by remember { mutableStateOf(false) }

    LaunchedEffect(discoveredUsers) {
        Log.d("DISCOVERED USERS", "$discoveredUsers")
        viewModel.filterDiscoveredUsers()
        Log.d("DISCOVERED USERS", "$filteredDiscoveredUsers")
    }

    if (showSuccessDialog) {
        Dialog(onDismissRequest = { showSuccessDialog = false }) {
            Box(
                modifier = Modifier
                    .background(color = Color.Green, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "User added to contacts !",
                    color = Color.White,
                )
            }
        }


        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(1000)
            showSuccessDialog = false
            navController.navigate("contactsscreen")
        }
    }

    ScreenTemplate(
        navIcon = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.secondary,
                    thickness = 2.dp,
                    modifier = Modifier
                        .alpha((0.6).toFloat())
                        .padding(vertical = 16.dp)
                        .width(LocalConfiguration.current.screenWidthDp.dp / 2)
                )
                Button(
                    onClick = {
                        if (!isVisible.value) {
                            if (localUser != null) {
                                viewModel.startAdvertising(localUser!!)
                                viewModel.startScan(localUser!!)
                            }
                        } else {
                            viewModel.stopAdvertising()
                            viewModel.stopScan()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isVisible.value) Color.Red else Color.Green
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (isVisible.value) "Hide" else "Show me",
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
            }
        },

        header = {
            Header(
                text = stringResource(R.string.available_around_you),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        },
        modifier = Modifier.swipeToNavigate(
            onSwipeLeft = {
                navController.navigate("contactsscreen")
            },
            onSwipeUp = {
                navController.navigate("settingsscreen")
            },
            coroutineScope = rememberCoroutineScope(),
            isNavigating = remember { mutableStateOf(false) },
            isSwipeHandled = remember { mutableStateOf(false) }
        )
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(filteredDiscoveredUsers.toList(), key = { it.deviceId }) { user ->
                CustomButton(
                    name = user.name,
                    image = {
                        if (user.avatar != null) {
                            CircledImage(bitmap = user.avatar, size = 48.dp)
                        } else {
                            CircledImage(bitmap = null, size = 48.dp)
                        }
                    },
                    backgroundColor = user.color,
                    onClick = {
                        Log.d("USER", "user: $user")
                        viewModel.addUserToLocalContacts(user)
                        showSuccessDialog = true
                    }
                )
            }
        }
    }
}
