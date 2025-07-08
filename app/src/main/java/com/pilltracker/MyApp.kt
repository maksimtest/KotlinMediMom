package com.pilltracker

import android.app.Application
import com.pilltracker.data.AppDatabase
import com.pilltracker.data.UserSettings

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
