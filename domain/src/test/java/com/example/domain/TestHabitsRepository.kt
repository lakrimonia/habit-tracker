package com.example.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

class TestHabitsRepository(private val habitsList: MutableList<Habit>) : HabitsRepository {
    private val mutableHabitToEdit: MutableStateFlow<Habit?> by lazy {
        MutableStateFlow(null)
    }
    override val habitToEdit: StateFlow<Habit?> = mutableHabitToEdit
    private lateinit var flow: Flow<List<Habit>>

    override suspend fun setHabitToEdit(habit: Habit?) {
        mutableHabitToEdit.value = habit
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
        habitsList.find { it == habit }?.apply { completionsCount++ }
    }
}