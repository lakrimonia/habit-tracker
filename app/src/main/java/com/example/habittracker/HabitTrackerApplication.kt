package com.example.habittracker

import android.app.Application
import com.example.habittracker.di.ApplicationComponent
import com.example.habittracker.di.ContextModule
import com.example.habittracker.di.DaggerApplicationComponent

class HabitTrackerApplication : Application() {
    lateinit var applicationComponent: ApplicationComponent
        private set

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent
            .builder()
            .contextModule(ContextModule(this))
            .build()
    }
}