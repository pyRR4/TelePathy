package com.example.telepathy

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.example.telepathy.data.DatabaseSeeder
import com.example.telepathy.data.PreferencesManager
import com.example.telepathy.data.entities.User
import com.example.telepathy.presentation.navigation.AnimatedNavHost
import com.example.telepathy.presentation.ui.theme.DarkUserColors
import com.example.telepathy.presentation.ui.theme.TelePathyTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private const val REQUEST_CODE_BLUETOOTH = 101

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Żądanie uprawnień Bluetooth
        getBluetoothPermissions(this)

        val context = applicationContext
        val preferencesManager = PreferencesManager(context)
        val database = AppDatabase.getDatabase(context)

        logSharedPreferences(context)

        CoroutineScope(Dispatchers.IO).launch {
            if (preferencesManager.isFirstLaunch()) {
                preferencesManager.setFirstLaunch(false)
                preferencesManager.savePin(null)

                val usersCount = database.userDao().getAllUsers().first().size
                if (usersCount == 0) {

                    preferencesManager.saveLocalUserId(1)

                    val defaultUser = User(
                        id = 0,
                        name = "Default User",
                        description = "This is the default user",
                        color = DarkUserColors.random(),
                        avatar = null
                    )
                    database.userDao().insert(defaultUser)
                    Log.d("Seed", "Created default user: $defaultUser")

                    DatabaseSeeder(database).seed()
                    Log.d("Seed", "Database seeded with sample data.")
                } else {
                    Log.d("Seed", "Database already contains users.")
                }
            }

            logSharedPreferences(context)
            logAllUsers(database)

            val locals = MutableStateFlow<User?>(null)

            database.userDao().getUser(preferencesManager.getLocalUserId()).collect { user ->
                locals.value = user
            }

            Log.d("locals", locals.value?.name ?: "NIE DZIALA")
        }

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


    private fun logSharedPreferences(context: Context) {
        val sharedPreferences = context.getSharedPreferences("telepathy_prefs", Context.MODE_PRIVATE)
        val allEntries = sharedPreferences.all
        Log.d("SharedPreferences", "Zawartość SharedPreferences:")
        for ((key, value) in allEntries) {
            Log.d("SharedPreferences", "Key: $key, Value: $value")
        }
    }

    private suspend fun logAllUsers(database: AppDatabase) {
        val allUsers = database.userDao().getAllUsers().first()
        Log.d("Database", "Zawartość bazy danych (Użytkownicy):")
        for (user in allUsers) {
            Log.d("Database", "User: $user")
        }
    }

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
        val currentScreen = remember { mutableStateOf("contacts") }
        val navController = rememberNavController()
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AnimatedNavHost(
                navController = navController,
                startDestination = "enter_pin_login",
                localUserId = preferencesManager.getLocalUserId(),
                currentScreen = currentScreen
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
