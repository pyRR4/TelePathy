package com.example.telepathy.data.repositories

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log

class BluetoothDiscoveryReceiver(private val onDeviceDiscovered: (BluetoothDevice) -> Unit) : BroadcastReceiver() {

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            BluetoothDevice.ACTION_FOUND -> {
                val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                device?.let {
                    Log.d("BluetoothDiscovery", "Device found: ${it.name} (${it.address})")
                    onDeviceDiscovered(it)
                }
            }
        }
    }

    companion object {
        val filter: IntentFilter
            get() = IntentFilter(BluetoothDevice.ACTION_FOUND)
    }
}
