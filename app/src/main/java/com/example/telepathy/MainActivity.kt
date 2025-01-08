package com.example.telepathy

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.telepathy.data.AppDatabase
import com.example.telepathy.data.PreferencesManager
import com.example.telepathy.presentation.navigation.AnimatedNavHost
import com.example.telepathy.presentation.ui.theme.TelePathyTheme

private const val REQUEST_CODE_BLUETOOTH = 101

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBluetoothPermissions(this)

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
        if (requestCode == REQUEST_CODE_BLUETOOTH) {
            permissions.forEachIndexed { index, permission ->
                val result = if (grantResults[index] == PackageManager.PERMISSION_GRANTED) "granted" else "denied"
                Log.d("BluetoothPermissions", "Permission: $permission -> $result")
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.S)
    private fun getBluetoothPermissions(activity: ComponentActivity) {
        val requiredPermissions = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            requiredPermissions.add(Manifest.permission.BLUETOOTH_SCAN)
        }
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            requiredPermissions.add(Manifest.permission.BLUETOOTH_CONNECT)
        }
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED) {
            requiredPermissions.add(Manifest.permission.BLUETOOTH_ADVERTISE)
        }
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requiredPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (requiredPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                activity,
                requiredPermissions.toTypedArray(),
                REQUEST_CODE_BLUETOOTH
            )
        } else {
            Log.d("BluetoothPermissions", "All required permissions already granted.")
        }
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
