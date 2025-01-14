package com.example.telepathy.presentation.viewmodels

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telepathy.data.AppDatabase
import com.example.telepathy.data.seeding.DatabaseSeeder
import com.example.telepathy.domain.PermissionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoadingViewModel(
    private val activity: ComponentActivity
) : ViewModel() {

    private val _isInitialized = MutableStateFlow<Boolean>(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()

    init {
        viewModelScope.launch {
            initApp()
        }
    }

    private suspend fun initApp() {
        val permissionManager = PermissionManager(activity)
        val database = AppDatabase.getDatabase(activity)
        val databaseSeeder = DatabaseSeeder(database, activity)

        withContext(Dispatchers.Main) {
            permissionManager.checkAndRequestBluetoothPermissions()
        }

        withContext(Dispatchers.IO) {
            databaseSeeder.seedDefaultUser()
        }

        _isInitialized.value = true
    }
}