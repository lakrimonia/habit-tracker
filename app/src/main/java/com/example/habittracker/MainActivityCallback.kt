package com.example.habittracker

interface MainActivityCallback {
    fun addHabit()
    fun editHabit(habit: Habit)
    fun addFinalHabit(habit: Habit)
    fun returnToMainPage()
}