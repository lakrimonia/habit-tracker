package com.example.habittracker.di

import com.example.habittracker.habitcreatingoredititng.HabitCreatingOrEditingFragment
import com.example.habittracker.mainpage.HabitsFilterAndSortingFragment
import com.example.habittracker.mainpage.HabitsListFragment
import com.example.habittracker.mainpage.MainPageFragment
import dagger.Subcomponent

@Subcomponent
interface HabitsListComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): HabitsListComponent
    }

    fun inject(mainPageFragment: MainPageFragment)
    fun inject(habitsListFragment: HabitsListFragment)
    fun inject(habitsFilterAndSortingFragment: HabitsFilterAndSortingFragment)
    fun inject(habitCreatingOrEditingFragment: HabitCreatingOrEditingFragment)
}