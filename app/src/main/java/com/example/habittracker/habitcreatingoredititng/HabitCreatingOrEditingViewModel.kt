package com.example.habittracker.habitcreatingoredititng

import androidx.lifecycle.*
import com.example.data.HabitRepository
import com.example.domain.Habit
import com.example.domain.HabitPriority
import com.example.domain.HabitType
import com.example.domain.InsertHabitUseCase
import com.example.domain.usecases.GetHabitToEditUseCase
import com.example.habittracker.Event
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.flow.collect

@Singleton
class HabitCreatingOrEditingViewModel @Inject constructor(
    private val insertHabitUseCase: InsertHabitUseCase,
    private val getHabitToEditUseCase: GetHabitToEditUseCase
) : ViewModel() {
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

    private var habitName: String = ""
    private var habitDescription: String = ""
    private var habitPriority: HabitPriority = defaultPriority
    private var habitType: HabitType = defaultType
    private var habitPeriodicityTimes: String = ""
    private var habitPeriodicityDays: String = ""
    private var habitColor: String = ""

    private var habit: Habit? = null


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
    private val mutableTimesEqualsZero by lazy {
        MutableLiveData<Boolean>()
    }
    val timesEqualsZero: LiveData<Boolean> = mutableTimesEqualsZero
    private val mutableDaysEqualsZero by lazy {
        MutableLiveData<Boolean>()
    }
    val daysEqualsZero: LiveData<Boolean> = mutableDaysEqualsZero

    val mediatorLiveData: MediatorLiveData<Habit> by lazy {
        MediatorLiveData()
    }

    init {
        viewModelScope.launch {
            getHabitToEditUseCase.getHabitToEdit().collect {
                if (it == null)
                    clearFields()
                else {
                    editHabit(it)
                    habit = it
                }
            }
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
        habitName = habit.name
        habitDescription = habit.description
        habitPriority = habit.priority
        habitType = habit.type
        habitPeriodicityTimes = habit.periodicityTimesPerDay.first.toString()
        habitPeriodicityDays = habit.periodicityTimesPerDay.second.toString()
        habitColor = habit.color.toString()
        setValues()
    }

    fun clearFields() {
        habit = null
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
        if (habitName == "" || habitPeriodicityTimes == "" || habitPeriodicityDays == ""
            || habitPeriodicityTimes.all { it == '0' } || habitPeriodicityDays.all { it == '0' }
        ) {
            mutableNameNotEntered.value = habitName == ""
            mutableTimesNotEntered.value = habitPeriodicityTimes == ""
            mutableDaysNotEntered.value = habitPeriodicityDays == ""
            if (habitPeriodicityTimes != "")
                mutableTimesEqualsZero.value = habitPeriodicityTimes.all { it == '0' }
            if (habitPeriodicityDays != "")
                mutableDaysEqualsZero.value = habitPeriodicityDays.all { it == '0' }
            return
        }

        if (habitDescription == "") habitDescription = defaultDescription
        habit?.let {
            val habit = Habit(
                habitName,
                habitDescription,
                habitPriority,
                habitType,
                habitPeriodicityTimes.toInt() to habitPeriodicityDays.toInt(),
                habitColor.toInt(),
                Calendar.getInstance().time.time,
                it.previousPeriodToCompletionsCount,
                it.currentPeriod,
                it.completionsCount,
                it.id
            )
            save(habit)
            mutableSaveChanges.value = Event(0)
            clearFields()
            return
        }
        val c = Calendar.getInstance()
        val now = c.time
        c.add(Calendar.DATE, habitPeriodicityDays.toInt())
        val end = c.time
        val habit = Habit(
            habitName,
            habitDescription,
            habitPriority,
            habitType,
            habitPeriodicityTimes.toInt() to habitPeriodicityDays.toInt(),
            habitColor.toInt(),
            now.time,
            mutableMapOf(),
            now to end,
            0,
            ""
        )
        save(habit)
        mutableSaveChanges.value = Event(0)
        clearFields()
    }

    private fun save(habit: Habit) =
        viewModelScope.launch {
            insertHabitUseCase.insertHabit(habit)
        }
}