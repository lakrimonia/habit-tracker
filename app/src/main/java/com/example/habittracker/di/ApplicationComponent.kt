package com.example.habittracker.di

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [HabitModule::class, ContextModule::class, SubcomponentModule::class])
interface ApplicationComponent {
    fun habitsListComponent(): HabitsListComponent.Factory
}