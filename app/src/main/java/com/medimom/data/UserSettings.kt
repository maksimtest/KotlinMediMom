package com.medimom.data

import android.content.Context
import android.content.SharedPreferences

class UserSettings(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "user_settings"
        private const val KEY_LANGUAGE = "language"
        private const val KEY_CURRENT_DATE = "current_date"
    }

    var language: String
        get() = prefs.getString(KEY_LANGUAGE, "en") ?: "en"
        set(value) = prefs.edit().putString(KEY_LANGUAGE, value).apply()

}
