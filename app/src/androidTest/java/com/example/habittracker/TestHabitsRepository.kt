package com.example.habittracker

import android.graphics.Color
import com.example.domain.Habit
import com.example.domain.HabitPriority
import com.example.domain.HabitType
import com.example.domain.HabitsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import java.util.*

class TestHabitsRepository : HabitsRepository {
    private val mutableHabitToEdit: MutableStateFlow<Habit?> by lazy {
        MutableStateFlow(null)
    }
    override val habitToEdit: StateFlow<Habit?> = mutableHabitToEdit
    private lateinit var habitsList: MutableList<Habit>
    private lateinit var flow: Flow<List<Habit>>

    init {
        GregorianCalendar(21, 5, 12).time
        habitsList = mutableListOf(
            Habit(
                "Спорт",
                "1 ч",
                HabitPriority.HIGH,
                HabitType.GOOD,
                3 to 7,
                Color.parseColor("#4df249"),
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
                Color.parseColor("#4df249"),
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
                Color.parseColor("#f24949"),
                GregorianCalendar(2021, 5, 11).time.time,
                mutableMapOf(),
                GregorianCalendar(2021, 5, 12).time to GregorianCalendar(2021, 5, 12).time,
                2,
                "2"
            ),
            Habit(
                "Прогуливать пары",
                "...",
                HabitPriority.HIGH,
                HabitType.BAD,
                1 to 30,
                Color.parseColor("#f24949"),
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
                Color.parseColor("#f24949"),
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
                Color.parseColor("#4df249"),
                GregorianCalendar(2021, 5, 10).time.time,
                mutableMapOf(),
                GregorianCalendar(2021, 5, 10).time to GregorianCalendar(2021, 5, 17).time,
                0,
                "5"
            )
        )
    }

    override suspend fun setHabitToEdit(habit: Habit?) {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(): Flow<List<Habit>> {
        flow = flow {
            emit(habitsList)
        }
        return flow
    }

    override suspend fun insert(habit: Habit) {
        habitsList.add(habit)
        habitsList.sortByDescending { it.priority }
    }

    override suspend fun delete(habit: Habit) {
        habitsList.remove(habit)
        habitsList.sortByDescending { it.priority }
    }

    override suspend fun markHabitAsCompleted(habit: Habit) {
        habit.completionsCount++
    }
}