package com.example.domain.usecases

import com.example.domain.HabitsRepository

class GetHabitToEditUseCase(private val habitsRepository: HabitsRepository) {
    fun getHabitToEdit() = habitsRepository.habitToEdit
}