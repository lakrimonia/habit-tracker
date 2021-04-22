package com.example.habittracker

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Habit(
    val id: Int,
    val name: String,
    val description: String,
    val priority: HabitPriority,
    val type: HabitType,
    val periodicityTimesPerDay: Pair<Int, Int>,
    val color: Int,
    val creatingDate: Calendar
) : Parcelable