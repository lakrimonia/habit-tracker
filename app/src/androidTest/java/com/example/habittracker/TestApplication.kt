package com.example.habittracker

import com.example.habittracker.di.*

class TestApplication : ApplicationWithDaggerComponent() {
    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.builder()
            .habitModule(HabitModule(TestHabitsRepository()))
            .contextModule(ContextModule(this))
            .build()
    }
}