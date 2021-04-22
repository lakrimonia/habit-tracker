package com.example.habittracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class HabitCreatingOrEditingViewModel : ViewModel() {
    private val _name by lazy {
        MutableLiveData<String>()
    }
    private val _description by lazy {
        MutableLiveData<String>()
    }
    private val _priority by lazy {
        MutableLiveData<HabitPriority>()
    }
    private val _type by lazy {
        MutableLiveData<HabitType>()
    }
    private val _periodicityTimes by lazy {
        MutableLiveData<Int>()
    }
    private val _periodicityDays by lazy {
        MutableLiveData<Int>()
    }
    private val _color by lazy {
        MutableLiveData<Int>()
    }
    private val _saveChanges = MutableLiveData<Event<Any>>()

    val name: LiveData<String> = _name
    val description: LiveData<String> = _description
    val priority: LiveData<HabitPriority> = _priority
    val type: LiveData<HabitType> = _type
    val periodicityTimes: LiveData<Int> = _periodicityTimes
    val periodicityDays: LiveData<Int> = _periodicityDays
    val color: LiveData<Int> = _color
    val saveChanges: LiveData<Event<Any>> = _saveChanges

    private var habitId: Int = 0

    private var habitToEditId: Int? = null
    private var habitName: String? = null
    private var habitDescription: String? = null
    private var habitPriority: HabitPriority? = null
    private var habitType: HabitType? = null
    private var habitPeriodicityTimes: Int? = null
    private var habitPeriodicityDays: Int? = null
    private var habitColor: Int? = null
    private var habitCreatingDate: Calendar? = null

    fun setName(name: String?) {
        habitName = name
    }

    fun setDescription(description: String?) {
        habitDescription = description
    }

    fun setPriority(priority: HabitPriority) {
        habitPriority = priority
    }

    fun setType(type: HabitType) {
        habitType = type
    }

    fun setPeriodicityTimes(periodicityTimes: Int?) {
        habitPeriodicityTimes = periodicityTimes
    }

    fun setPeriodicityDays(periodicityDays: Int?) {
        habitPeriodicityDays = periodicityDays
    }

    fun setColor(color: Int) {
        habitColor = color
    }

    fun setValues() {
        habitName.let { _name.value = it }
        habitDescription.let { _description.value = it }
        habitPriority.let { _priority.value = it }
        habitType.let { _type.value = it }
        habitPeriodicityTimes.let { _periodicityTimes.value = it }
        habitPeriodicityDays.let { _periodicityDays.value = it }
        habitColor.let { _color.value = it }
    }

    fun createHabit() {
        clearFields()
    }

    fun editHabit(habit: Habit) {
        habitToEditId = habit.id
        habitName = habit.name
        habitDescription = habit.description
        habitPriority = habit.priority
        habitType = habit.type
        habitPeriodicityTimes = habit.periodicityTimesPerDay.first
        habitPeriodicityDays = habit.periodicityTimesPerDay.second
        habitColor = habit.color
        habitCreatingDate = habit.creatingDate
    }

    private fun clearFields() {
        habitToEditId = null
        habitName = null
        habitDescription = null
        habitPriority = null
        habitType = null
        habitPeriodicityTimes = null
        habitPeriodicityDays = null
        habitColor = null
        habitCreatingDate = null
        setValues()
    }

    fun clickOnFab() {
        if (habitName == null || habitPeriodicityTimes == null || habitPeriodicityDays == null)
            _saveChanges.value = Event(0)
        val id = habitToEditId ?: habitId
        val creatingDate = habitCreatingDate ?: Calendar.getInstance()
        val habit = Habit(
            id,
            habitName!!,
            habitDescription ?: "",
            habitPriority ?: HabitPriority.HIGH,
            habitType ?: HabitType.GOOD,
            habitPeriodicityTimes!! to habitPeriodicityDays!!,
            habitColor!!,
            creatingDate
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
    }
}

