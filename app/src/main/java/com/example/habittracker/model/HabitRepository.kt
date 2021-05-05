package com.example.habittracker.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class HabitRepository(private val habitDao: HabitDao) {
    companion object {
        private val habitToEdit by lazy {
            MutableLiveData<Habit>()
        }
        val HABIT_TO_EDIT: LiveData<Habit> = habitToEdit

        fun setHabitToEdit(habit: Habit?) {
            habitToEdit.value = habit
        }
    }

    val allHabits = habitDao.getAll()

    suspend fun insert(habit: Habit) {
        habitDao.insert(habit)
        habitToEdit.postValue(null)
    }

    suspend fun update(habit: Habit) {
        habitDao.update(habit)
        habitToEdit.postValue(null)
    }

    suspend fun clear() {
        habitDao.clear()
    }
}