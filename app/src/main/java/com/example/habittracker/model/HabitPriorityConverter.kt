package com.example.habittracker.model

import androidx.room.TypeConverter

class HabitPriorityConverter {
    @TypeConverter
    fun fromHabitPriority(priority: HabitPriority): Int = priority.ordinal

    @TypeConverter
    fun toHabitPriority(ordinal: Int): HabitPriority = enumValues<HabitPriority>()[ordinal]
}