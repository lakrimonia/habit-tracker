package com.example.habittracker.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class HabitRepository(private val habitDao: HabitDao) {
    companion object {
        private val _habitToEdit by lazy {
            MutableLiveData<Habit>()
        }
        val HABIT_TO_EDIT: LiveData<Habit> = _habitToEdit

        fun setHabitToEdit(habit: Habit) {
            _habitToEdit.value = habit
        }
    }

    val allHabits = habitDao.getAll()
    val nextHabitId: Int
        get() = habitDao.getCount()

    fun insert(habit: Habit) {
        habitDao.insert(habit)
        _habitToEdit.value = null
    }

    fun clear() {
        habitDao.clear()
    }
}