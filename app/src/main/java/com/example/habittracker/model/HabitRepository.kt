package com.example.habittracker.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.habittracker.HabitUID
import com.example.habittracker.RetrofitClient

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

    val allHabits: LiveData<List<Habit>> = liveData {
        emitSource(habitDao.getAll())
        val habits = RetrofitClient.SERVICE.getHabits()
        habits.forEach { h -> habitDao.insert(h) }
    }

    suspend fun insert(habit: Habit) {
        val uid = RetrofitClient.SERVICE.addOrUpdateHabit(habit).uid
        habitDao.insert(
            Habit(
                habit.name,
                habit.description,
                habit.priority,
                habit.type,
                habit.periodicityTimesPerDay,
                habit.color,
                habit.changingDate,
                uid
            )
        )
        habitToEdit.postValue(null)
    }

    suspend fun update(habit: Habit) {
        habitDao.update(habit)
        habitToEdit.postValue(null)
    }

    suspend fun delete(habit: Habit) {
        RetrofitClient.SERVICE.deleteHabit(HabitUID(habit.id))
        habitDao.delete(habit)
    }

    suspend fun clear() {
        habitDao.clear()
    }
}