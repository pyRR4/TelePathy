package com.example.telepathy.domain

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

private const val REQUEST_CODE_BLUETOOTH = 101

class PermissionManager(private val activity: ComponentActivity) {

    fun checkAndRequestBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            getBluetoothPermissions()
        }
    }

    private fun getBluetoothPermissions() {
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
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requiredPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
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

    fun handlePermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_BLUETOOTH) {
            permissions.forEachIndexed { index, permission ->
                val result = if (grantResults[index] == PackageManager.PERMISSION_GRANTED) "granted" else "denied"
                Log.d("BluetoothPermissions", "Permission: $permission -> $result")
            }
        }
    }
}
