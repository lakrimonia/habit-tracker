package com.example.habittracker

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HabitsListViewModel() : ViewModel() {
    private val mutableHabits by lazy {
        MutableLiveData<ArrayList<Habit>>()
    }
    private val _startToCreateHabit by lazy {
        MutableLiveData<Event<Habit>>()
    }
    val startToCreateHabit: LiveData<Event<Habit>> = _startToCreateHabit
    private val _startToEditHabit by lazy {
        MutableLiveData<Event<Habit>>()
    }
    val startToEditHabit: LiveData<Event<Habit>> = _startToEditHabit
    val habits: LiveData<ArrayList<Habit>> = mutableHabits

    fun clickOnHabitItem(habit: Habit) {
        _startToEditHabit.value = Event(habit)
    }

    fun clickOnFab() {
        _startToCreateHabit.value = Event(
            Habit(
                0,
                "Studying",
                "Programming",
                HabitPriority.HIGH,
                HabitType.GOOD,
                1 to 1,
                Color.GREEN
            )
        )
    }

    fun updateHabitsList() {
        mutableHabits.value = HabitsList.habits
    }

    init {
        loadHabits()
    }

    private fun loadHabits() {
        mutableHabits.postValue(HabitsList.habits)
    }
}