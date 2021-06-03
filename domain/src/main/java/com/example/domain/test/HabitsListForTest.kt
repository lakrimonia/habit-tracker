package com.example.domain.test

import android.graphics.Color
import com.example.domain.Habit
import com.example.domain.HabitPriority
import com.example.domain.HabitType
import java.util.*

object HabitsListForTest {
    fun create(): List<Habit> {
        return listOf(
            Habit(
                "Спорт",
                "1 ч",
                HabitPriority.HIGH,
                HabitType.GOOD,
                3 to 7,
                Color.GREEN,
                GregorianCalendar(2021, 5, 12).time.time,
                mutableMapOf(),
                GregorianCalendar(2021, 5, 12).time to GregorianCalendar(2021, 5, 17).time,
                1,
                "0"
            ),
            Habit(
                "Вода",
                "1 л",
                HabitPriority.MEDIUM,
                HabitType.GOOD,
                2 to 1,
                Color.GREEN,
                GregorianCalendar(2021, 5, 10).time.time,
                mutableMapOf(),
                GregorianCalendar(2021, 5, 12).time to GregorianCalendar(2021, 5, 12).time,
                2,
                "1"
            ),
            Habit(
                "Гулять",
                "1 км",
                HabitPriority.LOW,
                HabitType.GOOD,
                5 to 1,
                Color.RED,
                GregorianCalendar(2021, 5, 11).time.time,
                mutableMapOf(),
                GregorianCalendar(2021, 5, 12).time to GregorianCalendar(2021, 5, 12).time,
                4,
                "2"
            ),
            Habit(
                "Прогуливать пары",
                "...",
                HabitPriority.HIGH,
                HabitType.BAD,
                1 to 30,
                Color.RED,
                GregorianCalendar(2021, 2, 1).time.time,
                mutableMapOf(),
                GregorianCalendar(2021, 5, 1).time to GregorianCalendar(2021, 6, 1).time,
                1,
                "3"
            ),
            Habit(
                "Алкоголь",
                "...",
                HabitPriority.MEDIUM,
                HabitType.BAD,
                1 to 30,
                Color.RED,
                GregorianCalendar(2021, 5, 1).time.time,
                mutableMapOf(),
                GregorianCalendar(2021, 5, 1).time to GregorianCalendar(2021, 6, 1).time,
                0,
                "4"
            ),
            Habit(
                "Фастфуд",
                "...",
                HabitPriority.LOW,
                HabitType.BAD,
                2 to 7,
                Color.GREEN,
                GregorianCalendar(2021, 5, 10).time.time,
                mutableMapOf(),
                GregorianCalendar(2021, 5, 10).time to GregorianCalendar(2021, 5, 17).time,
                0,
                "5"
            )
        )
    }
}