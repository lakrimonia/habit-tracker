package com.example.domain

class DeleteHabitUseCase(private val habitsRepository: HabitsRepository) {
    suspend fun deleteHabit(habit: Habit) = habitsRepository.delete(habit)
}