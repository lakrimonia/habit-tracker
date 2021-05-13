package com.example.habittracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "habit_table")
@TypeConverters(HabitPriorityConverter::class, PairConverter::class)
data class Habit(
    @ColumnInfo val name: String,
    @ColumnInfo val description: String,
    val priority: HabitPriority,
    @ColumnInfo val type: HabitType,
    val periodicityTimesPerDay: Pair<Int, Int>,
    @ColumnInfo val color: Int,
    @ColumnInfo val changingDate: Long,
    @PrimaryKey val id: String = ""
)

