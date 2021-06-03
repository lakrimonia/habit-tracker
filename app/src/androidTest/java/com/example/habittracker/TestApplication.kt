package com.example.habittracker

import com.example.domain.test.HabitsListForTest
import com.example.domain.test.TestHabitsRepository
import com.example.habittracker.di.*

class TestApplication : ApplicationWithDaggerComponent() {
    override fun onCreate() {
        super.onCreate()
        val habitsList = HabitsListForTest.create()
        val testRepository = TestHabitsRepository(habitsList.toMutableList())
        applicationComponent = DaggerApplicationComponent.builder()
            .habitModule(HabitModule(testRepository))
            .contextModule(ContextModule(this))
            .build()
    }
}