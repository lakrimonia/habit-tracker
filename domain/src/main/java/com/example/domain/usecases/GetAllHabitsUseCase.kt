package com.example.domain

import kotlinx.coroutines.flow.Flow

class GetAllHabitsUseCase(private val habitsRepository: HabitsRepository) {
    suspend fun getAllHabits(): Flow<List<Habit>> = habitsRepository.getAll()
}