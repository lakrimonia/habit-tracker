package com.example.domain

class MarkHabitAsCompletedUseCase(private val habitsRepository: HabitsRepository) {
    suspend fun markHabitAsCompleted(habit: Habit) = habitsRepository.markHabitAsCompleted(habit)
}