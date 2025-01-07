package com.example.telepathy.data

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "telepathy_prefs"
        private const val KEY_LOCAL_USER_ID = "local_user_id"
        private const val KEY_PIN = "user_pin"
        private const val KEY_IS_FIRST_LAUNCH = "is_first_launch"
    }

    fun saveLocalUserId(userId: Int) {
        sharedPreferences.edit()
            .putInt(KEY_LOCAL_USER_ID, userId)
            .apply()
    }

    fun getLocalUserId(): Int {
        return sharedPreferences.getInt(KEY_LOCAL_USER_ID, -1)
    }

    fun savePin(pin: String) {
        sharedPreferences.edit()
            .putString(KEY_PIN, pin)
            .apply()
    }

    fun getPin(): String? {
        return sharedPreferences.getString(KEY_PIN, null)
    }

    fun isFirstLaunch(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_FIRST_LAUNCH, true)
    }

    fun setFirstLaunch(isFirst: Boolean) {
        sharedPreferences.edit()
            .putBoolean(KEY_IS_FIRST_LAUNCH, isFirst)
            .apply()
    }
}
