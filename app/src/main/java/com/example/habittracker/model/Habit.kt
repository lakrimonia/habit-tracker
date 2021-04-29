package com.example.habittracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*

@Entity(tableName = "habit_table")
@TypeConverters(PairConverter::class, CalendarConverter::class)
data class Habit(
    @PrimaryKey val id: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val description: String,
    @ColumnInfo val priority: HabitPriority,
    @ColumnInfo val type: HabitType,
    val periodicityTimesPerDay: Pair<Int, Int>,
    @ColumnInfo val color: Int,
    val creatingDate: Calendar
)

