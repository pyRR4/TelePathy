package com.example.telepathy

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.telepathy.data.AppDatabase
import com.example.telepathy.data.PreferencesManager
import com.example.telepathy.domain.PermissionManager
import com.example.telepathy.presentation.navigation.AnimatedNavHost
import com.example.telepathy.presentation.ui.theme.TelePathyTheme

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
class MainActivity : ComponentActivity() {

    private lateinit var permissionManager: PermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionManager = PermissionManager(this)
        permissionManager.checkAndRequestBluetoothPermissions()

        val database = AppDatabase.getDatabase(applicationContext)

        setContent {
            MyApp()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionManager.handlePermissionsResult(requestCode, permissions, grantResults)
    }
}

@Composable
fun MyApp() {
    TelePathyTheme {
        val context = LocalContext.current

        val preferencesManager = PreferencesManager(context)
        val navController = rememberNavController()
        val currentScreen = remember { mutableStateOf("contacts") }
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AnimatedNavHost(
                navController = navController,
                startDestination = "enter_pin_login",
                currentScreen = currentScreen,
                localUserDeviceId = preferencesManager.getLocalUserDeviceId()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TelePathyPreview() {
    TelePathyTheme {
    }
}
