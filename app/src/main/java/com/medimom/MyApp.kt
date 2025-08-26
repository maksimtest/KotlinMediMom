package com.medimom

import android.app.Application
import com.medimom.data.AppDatabase
import com.medimom.data.UserSettings

class MyApp : Application() {
    lateinit var userSettings: UserSettings
    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getInstance(applicationContext)
        userSettings = UserSettings(applicationContext)
    }
}
