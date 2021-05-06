package com.example.habittracker.habitcreatingoredititng

import androidx.lifecycle.*
import com.example.habittracker.Event
import com.example.habittracker.model.Habit
import com.example.habittracker.model.HabitPriority
import com.example.habittracker.model.HabitRepository
import com.example.habittracker.model.HabitType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class HabitCreatingOrEditingViewModel(private val repository: HabitRepository) : ViewModel() {
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
    private val _saveChanges by lazy {
        MutableLiveData<Event<Any>>()
    }

    val name: LiveData<String> = _name
    val description: LiveData<String> = _description
    val priority: LiveData<HabitPriority> = _priority
    val type: LiveData<HabitType> = _type
    val periodicityTimes: LiveData<Int> = _periodicityTimes
    val periodicityDays: LiveData<Int> = _periodicityDays
    val color: LiveData<Int> = _color
    val saveChanges: LiveData<Event<Any>> = _saveChanges

    private var habitToEditId: Int? = null
    private var habitName: String? = null
    private var habitDescription: String? = null
    private var habitPriority: HabitPriority? = null
    private var habitType: HabitType? = null
    private var habitPeriodicityTimes: Int? = null
    private var habitPeriodicityDays: Int? = null
    private var habitColor: Int? = null
    private var habitCreatingDate: Calendar? = null

    private val _nameNotEntered by lazy {
        MutableLiveData<Boolean>()
    }
    val nameNotEntered: LiveData<Boolean> = _nameNotEntered
    private val _timesNotEntered by lazy {
        MutableLiveData<Boolean>()
    }
    val timesNotEntered: LiveData<Boolean> = _timesNotEntered
    private val _daysNotEntered by lazy {
        MutableLiveData<Boolean>()
    }
    val daysNotEntered: LiveData<Boolean> = _daysNotEntered

    val mediatorLiveData: MediatorLiveData<Habit> by lazy {
        MediatorLiveData()
    }

    init {
        mediatorLiveData.addSource(HabitRepository.HABIT_TO_EDIT) {
            if (it == null)
                clearFields()
            else
                editHabit(it)
        }
    }

    fun setName(name: String?) {
        if (name == "") {
            habitName = null
            return
        }
        habitName = name
        _nameNotEntered.value = false
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
        if (periodicityTimes == null)
            return
        _timesNotEntered.value = false
    }

    fun setPeriodicityDays(periodicityDays: Int?) {
        habitPeriodicityDays = periodicityDays
        if (periodicityDays == null)
            return
        _daysNotEntered.value = false
    }

    fun setColor(color: Int) {
        habitColor = color
    }

    fun setValues() {
        _name.value = habitName
        _description.value = habitDescription
        _priority.value = habitPriority
        _type.value = habitType
        _periodicityTimes.value = habitPeriodicityTimes
        _periodicityDays.value = habitPeriodicityDays
        _color.value = habitColor
    }

    private fun editHabit(habit: Habit) {
        habitToEditId = habit.id
        habitName = habit.name
        habitDescription = habit.description
        habitPriority = habit.priority
        habitType = habit.type
        habitPeriodicityTimes = habit.periodicityTimesPerDay.first
        habitPeriodicityDays = habit.periodicityTimesPerDay.second
        habitColor = habit.color
        habitCreatingDate = habit.creatingDate
        setValues()
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
        if (habitName == null || habitPeriodicityTimes == null || habitPeriodicityDays == null) {
            _nameNotEntered.value = habitName == null
            _timesNotEntered.value = habitPeriodicityTimes == null
            _daysNotEntered.value = habitPeriodicityDays == null
            return
        }
        viewModelScope.launch(Dispatchers.Default) {
            if (habitToEditId != null && habitCreatingDate != null) {
                val habit = Habit(
                    habitName!!,
                    habitDescription ?: "...",
                    habitPriority ?: HabitPriority.HIGH,
                    habitType ?: HabitType.GOOD,
                    habitPeriodicityTimes!! to habitPeriodicityDays!!,
                    habitColor!!,
                    habitCreatingDate!!,
                    habitToEditId!!
                )
                repository.update(habit)
            } else {
                val habit = Habit(
                    habitName!!,
                    habitDescription ?: "...",
                    habitPriority ?: HabitPriority.HIGH,
                    habitType ?: HabitType.GOOD,
                    habitPeriodicityTimes!! to habitPeriodicityDays!!,
                    habitColor!!,
                    Calendar.getInstance()
                )

                repository.insert(habit)
            }
        }
        _saveChanges.value = Event(0)
    }
}