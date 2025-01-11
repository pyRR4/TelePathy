package com.example.telepathy.presentation.ui.screens

import android.util.Log
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
    var isVisible by remember { mutableStateOf(false) }
    val discoveredUsers by viewModel.discoveredUsersDeviceIds.collectAsState()
    val localUser by sharedViewModel.localUser.collectAsState()
    val localUserId = localUser?.id ?: -1


    LaunchedEffect(isVisible, viewModel.discoveredUsersDeviceIds) {
        Log.d("LAUNCHED EFFECT", "Starting launched effect")
        sharedViewModel.loadLocalUser(localUserId)
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
                        isVisible = !isVisible
                        if (isVisible) {
                            if (localUser != null) {
                                viewModel.startAdvertising(localUser!!)
                            }
                            viewModel.startScan()
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
                        containerColor = if (isVisible) Color.Red else Color.Green
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (isVisible) "Hide" else "Show me",
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
                viewModel.stopAdvertising()
                viewModel.stopScan()
            },
            onSwipeUp = {
                navController.navigate("settingsscreen")
                viewModel.stopAdvertising()
                viewModel.stopScan()
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
            items(discoveredUsers) { user ->
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
                        navController.navigate("talkscreen/${user.id}")
                    }
                )
            }
        }
    }
}
