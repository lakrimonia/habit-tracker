package com.example.habittracker.mainpage

import androidx.lifecycle.*
import com.example.habittracker.Event
import com.example.habittracker.model.Habit
import com.example.habittracker.model.HabitRepository
import com.example.habittracker.model.HabitType

class HabitsListViewModel(private val repository: HabitRepository) : ViewModel() {
    val mediatorLiveData: MediatorLiveData<List<Habit>> by lazy {
        MediatorLiveData()
    }
    private val _resetFilters by lazy {
        MutableLiveData<Event<Int>>()
    }
    val resetFilters = _resetFilters
    private val _goodHabits by lazy {
        MutableLiveData<MutableList<Habit>>()
    }
    val goodHabits: LiveData<MutableList<Habit>> = _goodHabits
    private val _badHabits by lazy {
        MutableLiveData<MutableList<Habit>>()
    }
    val badHabits: LiveData<MutableList<Habit>> = _badHabits

    private val _startToCreateHabit by lazy {
        MutableLiveData<Event<Int>>()
    }
    val startToCreateHabit: LiveData<Event<Int>> = _startToCreateHabit
    private val _startToEditHabit by lazy {
        MutableLiveData<Event<Habit>>()
    }
    val startToEditHabit: LiveData<Event<Habit>> = _startToEditHabit

    private var sortedHabits: List<Habit>? = null
    private var filteredByNameHabits: List<Habit>? = null
    private var filteredByColorHabits: List<Habit>? = null

    private val colors: MutableList<Int> by lazy {
        mutableListOf()
    }

    init {
        _goodHabits.value = mutableListOf()
        _badHabits.value = mutableListOf()
        mediatorLiveData.addSource(repository.allHabits) {
            changeList(_goodHabits, it.filter { h -> h.type == HabitType.GOOD }.sortedByDescending { h -> h.priority })
            changeList(_badHabits, it.filter { h -> h.type == HabitType.BAD }.sortedByDescending { h -> h.priority })
            colors.clear()
            sortedHabits = it.sortedByDescending { h -> h.priority }
            filteredByNameHabits = it.sortedByDescending { h -> h.priority }
            filteredByColorHabits = it.sortedByDescending { h -> h.priority }
            _resetFilters.value = Event(1)
        }
    }

    fun clickOnHabitItem(habit: Habit) {
        HabitRepository.setHabitToEdit(habit)
        _startToEditHabit.value = Event(habit)
    }

    fun clickOnFab() {
        _startToCreateHabit.value = Event(0)
    }

    private fun changeList(habits: MutableLiveData<MutableList<Habit>>, items: Collection<Habit>) {
        habits.value?.clear()
        habits.value?.addAll(items)
        habits.value = habits.value
    }

    fun sortHabitsByPriorityAscending() {
        sortedHabits = sortedHabits?.sortedBy { it.priority }
        changeList(_goodHabits, sortedHabits!!.intersect(_goodHabits.value!!))
        changeList(_badHabits, sortedHabits!!.intersect(_badHabits.value!!))
    }

    fun sortHabitsByPriorityDescending() {
        sortedHabits = sortedHabits?.sortedByDescending { it.priority }
        changeList(_goodHabits, sortedHabits!!.intersect(_goodHabits.value!!))
        changeList(_badHabits, sortedHabits!!.intersect(_badHabits.value!!))
    }

    fun sortHabitsByNameAscending() {
        sortedHabits = sortedHabits?.sortedBy { it.name }
        changeList(_goodHabits, sortedHabits!!.intersect(_goodHabits.value!!))
        changeList(_badHabits, sortedHabits!!.intersect(_badHabits.value!!))
    }

    fun sortHabitsByNameDescending() {
        sortedHabits = sortedHabits?.sortedByDescending { it.name }
        changeList(_goodHabits, sortedHabits!!.intersect(_goodHabits.value!!))
        changeList(_badHabits, sortedHabits!!.intersect(_badHabits.value!!))
    }

    fun sortHabitsByCreatingDateAscending() {
        sortedHabits = sortedHabits?.sortedBy { it.creatingDate }
        changeList(_goodHabits, sortedHabits!!.intersect(_goodHabits.value!!))
        changeList(_badHabits, sortedHabits!!.intersect(_badHabits.value!!))
    }

    fun sortHabitsByCreatingDateDescending() {
        sortedHabits = sortedHabits?.sortedByDescending { it.creatingDate }
        changeList(_goodHabits, sortedHabits!!.intersect(_goodHabits.value!!))
        changeList(_badHabits, sortedHabits!!.intersect(_badHabits.value!!))
    }

    fun findByName(name: CharSequence) {
        repository.allHabits.value?.let {
            filteredByNameHabits = it.filter { h -> h.name.startsWith(name, true) }
            changeList(
                _goodHabits,
                sortedHabits?.intersect(it.filter { h -> h.type == HabitType.GOOD })
                    ?.intersect(filteredByColorHabits!!)?.intersect(filteredByNameHabits!!)!!
            )
            changeList(
                _badHabits,
                sortedHabits?.intersect(it.filter { h -> h.type == HabitType.BAD })
                    ?.intersect(filteredByColorHabits!!)?.intersect(filteredByNameHabits!!)!!
            )
        }
    }

    fun findByColor(color: Int) {
        colors.add(color)
        repository.allHabits.value?.let { allHabits ->
            filteredByColorHabits = allHabits.filter { colors.contains(it.color) }
            changeList(
                _goodHabits,
                sortedHabits?.intersect(allHabits.filter { h -> h.type == HabitType.GOOD })!!
                    .intersect(filteredByNameHabits!!)
                    .intersect(filteredByColorHabits!!)
            )
            changeList(
                _badHabits,
                sortedHabits?.intersect(allHabits.filter { h -> h.type == HabitType.BAD })!!
                    .intersect(filteredByNameHabits!!)
                    .intersect(filteredByColorHabits!!)
            )
        }
    }

    fun removeColor(color: Int) {
        colors.remove(color)
        repository.allHabits.value?.let { allHabits ->
            filteredByColorHabits = if (colors.isEmpty()) {
                allHabits
            } else {
                allHabits.filter { colors.contains(it.color) }
            }
            changeList(
                _goodHabits,
                sortedHabits?.intersect(allHabits.filter { h -> h.type == HabitType.GOOD })!!
                    .intersect(filteredByNameHabits!!).intersect(filteredByColorHabits!!)
            )
            changeList(
                _badHabits,
                sortedHabits?.intersect(allHabits.filter { h -> h.type == HabitType.BAD })!!
                    .intersect(filteredByNameHabits!!).intersect(filteredByColorHabits!!)
            )
        }
    }

    fun clickOnResetFilters() {
        _resetFilters.value = Event(1)
        resetFilters()
    }

    private fun resetFilters() {
        colors.clear()
        repository.allHabits.value?.let {
            sortedHabits = it.sortedByDescending { h -> h.priority }
            filteredByNameHabits = it.sortedByDescending { h -> h.priority }
            filteredByColorHabits = it.sortedByDescending { h -> h.priority }
            changeList(_goodHabits, it.filter { h -> h.type == HabitType.GOOD }.sortedByDescending { h -> h.priority })
            changeList(_badHabits, it.filter { h -> h.type == HabitType.BAD }.sortedByDescending { h -> h.priority })
        }
    }
}