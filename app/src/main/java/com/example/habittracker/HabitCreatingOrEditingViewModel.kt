package com.example.habittracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HabitCreatingOrEditingViewModel : ViewModel() {
    private val _saveChanges = MutableLiveData<Event<Habit>>()
    private val _habitToEdit = MutableLiveData<Habit>()
    val saveChanges: LiveData<Event<Habit>> = _saveChanges
    var habitToEdit: LiveData<Habit> = _habitToEdit
    private var habitId: Int = 0

    fun editHabit(habit: Habit) {
        _habitToEdit.value = habit
    }

    fun clickOnFab(
        name: String,
        description: String,
        priority: HabitPriority,
        type: HabitType,
        periodicityTimesPerDay: Pair<Int, Int>,
        color: Int
    ) {
        var id = habitId
        habitToEdit.value?.let {
            id = it.id
        }
        val habit = Habit(
            id,
            name,
            description,
            priority,
            type,
            periodicityTimesPerDay,
            color
        )
        val habits = HabitsList.habits

        if (habit.id != habitId) {
            var deletedHabitIndex = 0
            for (i in 0 until habits.size) {
                if (habits[i].id == habit.id) {
                    deletedHabitIndex = i
                    break
                }
            }
            habits.removeAt(deletedHabitIndex)
        } else habitId++
        when (habit.priority) {
            HabitPriority.HIGH -> habits.add(0, habit)
            HabitPriority.MEDIUM -> habits.add(habits.size / 2, habit)
            HabitPriority.LOW -> habits.add(habit)
        }
        _saveChanges.value = Event(habit)
        _habitToEdit.value = null
    }
}

