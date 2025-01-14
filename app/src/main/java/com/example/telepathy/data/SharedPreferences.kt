package com.example.telepathy.data

import android.content.Context
import android.content.SharedPreferences
import java.util.UUID

class PreferencesManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "telepathy_prefs"
        private const val KEY_LOCAL_USER_DEVICE_ID = "local_user_device_id"
        private const val KEY_PIN = "user_pin"
        private const val KEY_LOCAL_USER_ID = "local_user_id"
    }

    fun saveLocalUserDeviceId(userDeviceId: String) {
        sharedPreferences.edit()
            .putString(KEY_LOCAL_USER_DEVICE_ID, userDeviceId)
            .apply()
    }

    fun getLocalUserDeviceId(): String {
        return sharedPreferences.getString(KEY_LOCAL_USER_DEVICE_ID, "") ?: ""
    }

    fun saveLocalUserId(userId: Int) {
        sharedPreferences.edit()
            .putInt(KEY_LOCAL_USER_ID, userId)
            .apply()
    }

    fun getLocalUserId(): Int {
        return sharedPreferences.getInt(KEY_LOCAL_USER_ID, -1)
    }

    fun savePin(pin: String?) {
        sharedPreferences.edit()
            .putString(KEY_PIN, pin)
            .apply()
    }

    fun getPin(): String? {
        return sharedPreferences.getString(KEY_PIN, null)
    }

    fun clear() {
        sharedPreferences.edit()
            .clear()
            .apply()
    }
}
