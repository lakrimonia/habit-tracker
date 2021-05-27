package com.example.domain

class InsertHabitUseCase(private val habitsRepository: HabitsRepository) {
    suspend fun insertHabit(habit: Habit) = habitsRepository.insert(habit)
}