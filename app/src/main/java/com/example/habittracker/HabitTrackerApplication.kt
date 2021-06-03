package com.example.habittracker

import com.example.habittracker.di.ContextModule
import com.example.habittracker.di.DaggerApplicationComponent

class HabitTrackerApplication : ApplicationWithDaggerComponent() {
    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent
            .builder()
            .contextModule(ContextModule(this))
            .build()
    }
}