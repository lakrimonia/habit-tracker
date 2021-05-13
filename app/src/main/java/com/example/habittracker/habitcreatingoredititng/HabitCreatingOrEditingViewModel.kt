package com.example.habittracker.habitcreatingoredititng

import androidx.lifecycle.*
import com.example.habittracker.Event
import com.example.habittracker.model.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext

class HabitCreatingOrEditingViewModel(private val repository: HabitRepository) : ViewModel(), CoroutineScope {
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

    private val defaultDescription = "..."
    private val defaultPriority = HabitPriority.HIGH
    private val defaultType = HabitType.GOOD

    private var habitToEditId: String? = null
    private var habitName: String = ""
    private var habitDescription: String = ""
    private var habitPriority: HabitPriority = defaultPriority
    private var habitType: HabitType = defaultType
    private var habitPeriodicityTimes: String = ""
    private var habitPeriodicityDays: String = ""
    private var habitColor: String = ""

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

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job + CoroutineExceptionHandler { _, e -> throw e }

    init {
        mediatorLiveData.addSource(HabitRepository.HABIT_TO_EDIT) {
            if (it == null)
                clearFields()
            else
                editHabit(it)
        }
    }

    fun setName(name: String) {
        habitName = name
        if (name == "")
            return
        mutableNameNotEntered.value = false
    }

    fun setDescription(description: String) {
        habitDescription = description
    }

    fun setPriority(priority: HabitPriority?) {
        habitPriority = priority ?: defaultPriority
    }

    fun setType(type: HabitType?) {
        habitType = type ?: defaultType
    }

    fun setPeriodicityTimes(periodicityTimes: String) {
        habitPeriodicityTimes = periodicityTimes
        if (periodicityTimes == "")
            return
        mutableTimesNotEntered.value = false
    }

    fun setPeriodicityDays(periodicityDays: String) {
        habitPeriodicityDays = periodicityDays
        if (periodicityDays == "")
            return
        mutableDaysNotEntered.value = false
    }

    fun setColor(color: String) {
        habitColor = color
    }

    fun setValues() {
        mutableName.value = habitName
        mutableDescription.value = habitDescription
        mutablePriority.value = habitPriority
        mutableType.value = habitType
        mutablePeriodicityTimes.value =
            if (habitPeriodicityTimes != "") habitPeriodicityTimes.toInt() else null
        mutablePeriodicityDays.value =
            if (habitPeriodicityDays != "") habitPeriodicityDays.toInt() else null
        mutableColor.value = if (habitColor != "") habitColor.toInt() else null
    }

    private fun editHabit(habit: Habit) {
        habitToEditId = habit.id
        habitName = habit.name
        habitDescription = habit.description
        habitPriority = habit.priority
        habitType = habit.type
        habitPeriodicityTimes = habit.periodicityTimesPerDay.first.toString()
        habitPeriodicityDays = habit.periodicityTimesPerDay.second.toString()
        habitColor = habit.color.toString()
        setValues()
    }

    private fun clearFields() {
        habitToEditId = null
        habitName = ""
        habitDescription = ""
        habitPriority = defaultPriority
        habitType = defaultType
        habitPeriodicityTimes = ""
        habitPeriodicityDays = ""
        habitColor = ""
        setValues()
    }

    fun clickOnFab() {
        if (habitName == "" || habitPeriodicityTimes == "" || habitPeriodicityDays == "") {
            mutableNameNotEntered.value = habitName == ""
            mutableTimesNotEntered.value = habitPeriodicityTimes == ""
            mutableDaysNotEntered.value = habitPeriodicityDays == ""
            return
        }
        if (habitDescription == "") habitDescription = defaultDescription
        val habit = Habit(
            habitName,
            habitDescription,
            habitPriority,
            habitType,
            habitPeriodicityTimes.toInt() to habitPeriodicityDays.toInt(),
            habitColor.toInt(),
            Calendar.getInstance().time.time,
            habitToEditId ?: ""
        )
        save(habit)
        mutableSaveChanges.value = Event(0)
    }

    private fun save(habit: Habit) =
        launch {
            repository.insert(habit)
        }
}