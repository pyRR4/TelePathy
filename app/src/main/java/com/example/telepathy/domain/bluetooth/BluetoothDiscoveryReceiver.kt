package com.example.telepathy.domain.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.ParcelUuid
import android.util.Log
import androidx.annotation.RequiresApi
import java.util.UUID

class BluetoothDiscoveryReceiver(
    private val onDeviceFound: (BluetoothDevice) -> Unit,
    private val onUuidFetched: (BluetoothDevice, List<UUID>) -> Unit
) : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("RECEIVER", "Received: $intent")
        when (intent.action) {
            BluetoothDevice.ACTION_FOUND -> {
                val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE,
                    BluetoothDevice::class.java)
                device?.let { onDeviceFound(it) }
            }
            BluetoothDevice.ACTION_UUID -> {
                val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
                val uuids: Array<ParcelUuid>? = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID, BluetoothDevice::class.java) as? Array<ParcelUuid>
                if (device != null && uuids != null) {
                    onUuidFetched(device, uuids.map { it.uuid })
                }
            }
        }
    }

    companion object {
        val filter: IntentFilter
            get() = IntentFilter().apply {
                addAction(BluetoothDevice.ACTION_FOUND)
                addAction(BluetoothDevice.ACTION_UUID)
                addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            }
    }
}
