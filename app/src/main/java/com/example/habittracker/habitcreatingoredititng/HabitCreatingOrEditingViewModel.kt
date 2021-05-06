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
    private val mutableName by lazy {
        MutableLiveData<String>()
    }
    private val mutableDescription by lazy {
        MutableLiveData<String>()
    }
    private val mutablePriority by lazy {
        MutableLiveData<HabitPriority>()
    }
    private val mutableType by lazy {
        MutableLiveData<HabitType>()
    }
    private val mutablePeriodicityTimes by lazy {
        MutableLiveData<Int>()
    }
    private val mutablePeriodicityDays by lazy {
        MutableLiveData<Int>()
    }
    private val mutableColor by lazy {
        MutableLiveData<Int>()
    }
    private val mutableSaveChanges by lazy {
        MutableLiveData<Event<Any>>()
    }

    val name: LiveData<String> = mutableName
    val description: LiveData<String> = mutableDescription
    val priority: LiveData<HabitPriority> = mutablePriority
    val type: LiveData<HabitType> = mutableType
    val periodicityTimes: LiveData<Int> = mutablePeriodicityTimes
    val periodicityDays: LiveData<Int> = mutablePeriodicityDays
    val color: LiveData<Int> = mutableColor
    val saveChanges: LiveData<Event<Any>> = mutableSaveChanges

    private var habitToEditId: Int? = null
    private var habitName: String? = null
    private var habitDescription: String? = null
    private var habitPriority: HabitPriority? = null
    private var habitType: HabitType? = null
    private var habitPeriodicityTimes: Int? = null
    private var habitPeriodicityDays: Int? = null
    private var habitColor: Int? = null
    private var habitCreatingDate: Calendar? = null

//    private var habitToEditId: String = ""
//    private var habitName: String = ""
//    private var habitDescription: String = ""
//    private var habitPriority: String = ""
//    private var habitType: String = ""
//    private var habitPeriodicityTimes: String = ""
//    private var habitPeriodicityDays: String = ""
//    private var habitColor: String = ""
//    private var habitCreatingDate: String = ""

    private val mutableNameNotEntered by lazy {
        MutableLiveData<Boolean>()
    }
    val nameNotEntered: LiveData<Boolean> = mutableNameNotEntered
    private val mutableTimesNotEntered by lazy {
        MutableLiveData<Boolean>()
    }
    val timesNotEntered: LiveData<Boolean> = mutableTimesNotEntered
    private val mutableDaysNotEntered by lazy {
        MutableLiveData<Boolean>()
    }
    val daysNotEntered: LiveData<Boolean> = mutableDaysNotEntered

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
        mutableNameNotEntered.value = false
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
        mutableTimesNotEntered.value = false
    }

    fun setPeriodicityDays(periodicityDays: Int?) {
        habitPeriodicityDays = periodicityDays
        if (periodicityDays == null)
            return
        mutableDaysNotEntered.value = false
    }

    fun setColor(color: Int) {
        habitColor = color
    }

    fun setValues() {
        mutableName.value = habitName
        mutableDescription.value = habitDescription
        mutablePriority.value = habitPriority
        mutableType.value = habitType
        mutablePeriodicityTimes.value = habitPeriodicityTimes
        mutablePeriodicityDays.value = habitPeriodicityDays
        mutableColor.value = habitColor
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
            mutableNameNotEntered.value = habitName == null
            mutableTimesNotEntered.value = habitPeriodicityTimes == null
            mutableDaysNotEntered.value = habitPeriodicityDays == null
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
        mutableSaveChanges.value = Event(0)
    }
}