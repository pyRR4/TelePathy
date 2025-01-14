package com.example.telepathy.presentation.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telepathy.data.AppDatabase
import com.example.telepathy.data.PreferencesManager
import com.example.telepathy.data.seeding.DatabaseSeeder
import com.example.telepathy.domain.PermissionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoadingViewModel(private val activity: ComponentActivity) : ViewModel() {

    private val _isInitializedDatabase = MutableStateFlow<Boolean>(false)
    val isInitializedDatabase: StateFlow<Boolean> = _isInitializedDatabase.asStateFlow()

    private val _isInitializedPermissions = MutableStateFlow<Boolean>(false)
    val isInitializedPermissions: StateFlow<Boolean> = _isInitializedPermissions.asStateFlow()

    init {
        viewModelScope.launch {
            initApp()
        }
    }

    private suspend fun initApp() {
        val permissionManager = PermissionManager(activity)
        val database = AppDatabase.getDatabase(activity)
        val seeder = DatabaseSeeder(database, activity)

        withContext(Dispatchers.Default) {
            permissionManager.checkAndRequestBluetoothPermissions()
            _isInitializedPermissions.value = true
        }

        withContext(Dispatchers.IO) {
            seeder.seed()
            _isInitializedDatabase.value = true
        }
    }
}