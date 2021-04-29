package com.example.habittracker.mainpage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habittracker.model.HabitRepository

class HabitsListViewModelFactory(private val repository: HabitRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HabitsListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HabitsListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}