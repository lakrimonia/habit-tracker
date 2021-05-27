package com.example.data.converters

import androidx.room.TypeConverter
import com.example.domain.HabitPriority

class HabitPriorityConverter {
    @TypeConverter
    fun fromHabitPriority(priority: HabitPriority): Int = priority.ordinal

    @TypeConverter
    fun toHabitPriority(ordinal: Int): HabitPriority = enumValues<HabitPriority>()[ordinal]
}