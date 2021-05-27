package com.example.domain

import kotlinx.coroutines.flow.first

class FilterAndSortHabitsUseCase(private val habitsRepository: HabitsRepository) {
    suspend fun getHabitsSortedByPriorityAscending(): List<Habit> {
        return habitsRepository.getAll().first().sortedBy { it.priority }
    }

    suspend fun getHabitsSortedByPriorityDescending(): List<Habit> {
        return habitsRepository.getAll().first().sortedByDescending { it.priority }
    }

    suspend fun getHabitsSortedByNameAscending(): List<Habit> {
        return habitsRepository.getAll().first().sortedBy { it.name }
    }

    suspend fun getHabitsSortedByNameDescending(): List<Habit> {
        return habitsRepository.getAll().first().sortedByDescending { it.name }
    }

    suspend fun getHabitsSortedByChangingDateAscending(): List<Habit> {
        return habitsRepository.getAll().first().sortedBy { it.changingDate }
    }

    suspend fun getHabitsSortedByChangingDateDescending(): List<Habit> {
        return habitsRepository.getAll().first().sortedByDescending { it.changingDate }
    }

    suspend fun getHabitsFilteredByName(name: CharSequence): List<Habit> {
        return habitsRepository.getAll().first().filter { it.name.startsWith(name, true) }
    }

    suspend fun getHabitsFilteredByColor(colors: Collection<Int>): List<Habit> {
        val allHabits = habitsRepository.getAll().first()
        return if (colors.isEmpty()) allHabits else allHabits.filter { colors.contains(it.color) }
    }
}