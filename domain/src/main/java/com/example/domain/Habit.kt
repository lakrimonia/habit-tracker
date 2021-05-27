package com.example.domain

import java.util.*

data class Habit(
    val name: String,
    val description: String,
    val priority: HabitPriority,
    val type: HabitType,
    val periodicityTimesPerDay: Pair<Int, Int>,
    val color: Int,
    val changingDate: Long,
    val previousPeriodToCompletionsCount: MutableMap<Pair<Date, Date>, Int>,
    val currentPeriod: Pair<Date, Date>,
    var completionsCount: Int,
    val id: String = ""
)