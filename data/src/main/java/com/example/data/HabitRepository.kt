package com.example.data

import com.example.domain.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class HabitRepository(private val habitDao: HabitDao, private val habitApi: HabitApi) :
    HabitsRepository {
    private val mutableHabitToEdit: MutableStateFlow<Habit?> by lazy {
        MutableStateFlow(null)
    }
    override val habitToEdit: StateFlow<Habit?> = mutableHabitToEdit

    init {
        GlobalScope.launch {
            val allHabits = habitApi.getHabits()
            allHabits.forEach { habitDao.insert(HabitEntity.from(it)) }
        }
    }

    override suspend fun setHabitToEdit(habit: Habit?) {
        mutableHabitToEdit.value = habit
    }

    override suspend fun getAll(): Flow<List<Habit>> {
        return habitDao.getAll().transform { a ->
            val res = mutableListOf<Habit>()
            a.forEach { habitEntity -> res.add(habitEntity.toHabit()) }
            emit(res.toList())
        }
    }

    override suspend fun insert(habit: Habit) {
        val uid = habitApi.addOrUpdateHabit(habit)
        habitDao.insert(
            HabitEntity(
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
                uid.uid
            )
        )
        setHabitToEdit(null)
    }

    override suspend fun delete(habit: Habit) {
        habitApi.deleteHabit(HabitUID(habit.id))
        habitDao.delete(HabitEntity.from(habit))
    }

    override suspend fun markHabitAsCompleted(habit: Habit) {
        val doneDate = Calendar.getInstance().time.time
        habitApi.markHabitCompleted(HabitDone(doneDate, habit.id))
        habit.completionsCount++
        habitDao.getHabitById(habit.id).completionsCount++
    }
}