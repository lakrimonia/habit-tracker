package com.example.habittracker

class HabitsList {
    companion object Singleton {
        val habits: ArrayList<Habit> by lazy {
            ArrayList()
        }
    }
}