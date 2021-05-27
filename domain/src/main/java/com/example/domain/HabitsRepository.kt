package com.example.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface HabitsRepository {
    val habitToEdit: StateFlow<Habit?>

    suspend fun setHabitToEdit(habit: Habit?)
    suspend fun getAll(): Flow<List<Habit>>
    suspend fun insert(habit: Habit)
    suspend fun delete(habit: Habit)
    suspend fun markHabitAsCompleted(habit: Habit)
}