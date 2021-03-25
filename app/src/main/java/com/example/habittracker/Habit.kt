package com.example.habittracker

class Habit(
    val id: Int,
    val name: String,
    val description: String,
    val priority: HabitPriority,
    val type: HabitType,
    val periodicityTimesPerDay: Pair<Int, Int>,
    val color: Int
) {

}