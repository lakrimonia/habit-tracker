package com.example.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.data.converters.*
import com.example.domain.Habit
import com.example.domain.HabitPriority
import com.example.domain.HabitType
import java.util.*

@Entity(tableName = "habit_table")
@TypeConverters(
    HabitPriorityConverter::class,
    PairConverter::class,
    DatePairConverter::class,
    MutableMapConverter::class
)
data class HabitEntity(
    @ColumnInfo val name: String,
    @ColumnInfo val description: String,
    val priority: HabitPriority,
    @ColumnInfo val type: HabitType,
    val periodicityTimesPerDay: Pair<Int, Int>,
    @ColumnInfo val color: Int,
    @ColumnInfo val changingDate: Long,
    val previousPeriodToCompletionsCount: MutableMap<Pair<Date, Date>, Int>,
    val currentPeriod: Pair<Date, Date>,
    @ColumnInfo var completionsCount: Int,
    @PrimaryKey val id: String = ""
) {
    companion object {
        fun from(habit: Habit): HabitEntity {
            return HabitEntity(
                habit.name,
                habit.description,
                habit.priority,
                habit.type,
                habit.periodicityTimesPerDay,
                habit.color,
                habit.changingDate,
                habit.previousPeriodToCompletionsCount,
                habit.currentPeriod,
                habit.completionsCount,
                habit.id
            )
        }
    }

    fun toHabit(): Habit {
        return Habit(
            name,
            description,
            priority,
            type,
            periodicityTimesPerDay,
            color,
            changingDate,
            previousPeriodToCompletionsCount,
            currentPeriod,
            completionsCount,
            id
        )
    }
}