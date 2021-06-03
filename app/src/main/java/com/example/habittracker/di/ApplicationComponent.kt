package com.example.habittracker.di

import com.example.domain.*
import com.example.domain.usecases.EditHabitUseCase
import com.example.domain.usecases.GetHabitToEditUseCase
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    HabitModule::class,
    ContextModule::class,
    SubcomponentModule::class
])
interface ApplicationComponent {
    fun habitsListComponent(): HabitsListComponent.Factory
}