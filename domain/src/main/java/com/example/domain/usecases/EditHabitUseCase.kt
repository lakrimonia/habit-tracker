package com.example.domain.usecases

import com.example.domain.Habit
import com.example.domain.HabitsRepository

class EditHabitUseCase(private val habitsRepository: HabitsRepository) {
    suspend fun editHabit(habit: Habit?) = habitsRepository.setHabitToEdit(habit)
}