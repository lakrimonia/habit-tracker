package com.example.habittracker.mainpage

import androidx.lifecycle.*
import com.example.habittracker.Event
import com.example.habittracker.RetrofitClient
import com.example.habittracker.model.Habit
import com.example.habittracker.model.HabitRepository
import com.example.habittracker.model.HabitType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HabitsListViewModel(private val repository: HabitRepository) : ViewModel() {
    val mediatorLiveData: MediatorLiveData<List<Habit>> by lazy {
        MediatorLiveData()
    }
    private val mutableResetFilters by lazy {
        MutableLiveData<Event<Int>>()
    }
    val resetFilters = mutableResetFilters
    private val mutableGoodHabits by lazy {
        MutableLiveData<MutableList<Habit>>()
    }
    val goodHabits: LiveData<MutableList<Habit>> = mutableGoodHabits
    private val mutableBadHabits by lazy {
        MutableLiveData<MutableList<Habit>>()
    }
    val badHabits: LiveData<MutableList<Habit>> = mutableBadHabits

    private val mutableStartToCreateHabit by lazy {
        MutableLiveData<Event<Int>>()
    }
    val startToCreateHabit: LiveData<Event<Int>> = mutableStartToCreateHabit
    private val mutableStartToEditHabit by lazy {
        MutableLiveData<Event<Habit>>()
    }
    val startToEditHabit: LiveData<Event<Habit>> = mutableStartToEditHabit

    private var sortedHabits: List<Habit>? = null
    private var filteredByNameHabits: List<Habit>? = null
    private var filteredByColorHabits: List<Habit>? = null

    private val colors: MutableList<Int> by lazy {
        mutableListOf()
    }

    init {
        mutableGoodHabits.value = mutableListOf()
        mutableBadHabits.value = mutableListOf()
        viewModelScope.launch(Dispatchers.Default) {
            val habits = RetrofitClient.SERVICE.getHabits()
            habits.forEach { repository.insert(it) }
        }
        mediatorLiveData.addSource(repository.allHabits) {
            changeList(mutableGoodHabits, it.filter { h -> h.type == HabitType.GOOD })
            changeList(mutableBadHabits, it.filter { h -> h.type == HabitType.BAD })
            colors.clear()
            sortedHabits = it
            filteredByNameHabits = it
            filteredByColorHabits = it
            mutableResetFilters.value = Event(1)
        }
    }

    fun clickOnHabitItem(habit: Habit) {
        HabitRepository.setHabitToEdit(habit)
        mutableStartToEditHabit.value = Event(habit)
    }

    fun clickOnFab() {
        mutableStartToCreateHabit.value = Event(0)
    }

    private fun changeList(habits: MutableLiveData<MutableList<Habit>>, items: Collection<Habit>) {
        habits.value?.clear()
        habits.value?.addAll(items)
        habits.value = habits.value
    }

    fun sortHabitsByPriorityAscending() {
        sortedHabits = sortedHabits?.sortedBy { it.priority }
        changeList(mutableGoodHabits, sortedHabits!!.intersect(mutableGoodHabits.value!!))
        changeList(mutableBadHabits, sortedHabits!!.intersect(mutableBadHabits.value!!))
    }

    fun sortHabitsByPriorityDescending() {
        sortedHabits = sortedHabits?.sortedByDescending { it.priority }
        changeList(mutableGoodHabits, sortedHabits!!.intersect(mutableGoodHabits.value!!))
        changeList(mutableBadHabits, sortedHabits!!.intersect(mutableBadHabits.value!!))
    }

    fun sortHabitsByNameAscending() {
        sortedHabits = sortedHabits?.sortedBy { it.name }
        changeList(mutableGoodHabits, sortedHabits!!.intersect(mutableGoodHabits.value!!))
        changeList(mutableBadHabits, sortedHabits!!.intersect(mutableBadHabits.value!!))
    }

    fun sortHabitsByNameDescending() {
        sortedHabits = sortedHabits?.sortedByDescending { it.name }
        changeList(mutableGoodHabits, sortedHabits!!.intersect(mutableGoodHabits.value!!))
        changeList(mutableBadHabits, sortedHabits!!.intersect(mutableBadHabits.value!!))
    }

    fun sortHabitsByCreatingDateAscending() {
        sortedHabits = sortedHabits?.sortedBy { it.creatingDate }
        changeList(mutableGoodHabits, sortedHabits!!.intersect(mutableGoodHabits.value!!))
        changeList(mutableBadHabits, sortedHabits!!.intersect(mutableBadHabits.value!!))
    }

    fun sortHabitsByCreatingDateDescending() {
        sortedHabits = sortedHabits?.sortedByDescending { it.creatingDate }
        changeList(mutableGoodHabits, sortedHabits!!.intersect(mutableGoodHabits.value!!))
        changeList(mutableBadHabits, sortedHabits!!.intersect(mutableBadHabits.value!!))
    }

    fun findByName(name: CharSequence) {
        repository.allHabits.value?.let {
            filteredByNameHabits = it.filter { h -> h.name.startsWith(name, true) }
            changeList(
                mutableGoodHabits,
                sortedHabits?.intersect(it.filter { h -> h.type == HabitType.GOOD })
                    ?.intersect(filteredByColorHabits!!)?.intersect(filteredByNameHabits!!)!!
            )
            changeList(
                mutableBadHabits,
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
                mutableGoodHabits,
                sortedHabits?.intersect(allHabits.filter { h -> h.type == HabitType.GOOD })!!
                    .intersect(filteredByNameHabits!!)
                    .intersect(filteredByColorHabits!!)
            )
            changeList(
                mutableBadHabits,
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
                mutableGoodHabits,
                sortedHabits?.intersect(allHabits.filter { h -> h.type == HabitType.GOOD })!!
                    .intersect(filteredByNameHabits!!).intersect(filteredByColorHabits!!)
            )
            changeList(
                mutableBadHabits,
                sortedHabits?.intersect(allHabits.filter { h -> h.type == HabitType.BAD })!!
                    .intersect(filteredByNameHabits!!).intersect(filteredByColorHabits!!)
            )
        }
    }

    fun clickOnResetFilters() {
        mutableResetFilters.value = Event(1)
        resetFilters()
    }

    private fun resetFilters() {
        colors.clear()
        repository.allHabits.value?.let {
            sortedHabits = it
            filteredByNameHabits = it
            filteredByColorHabits = it
            changeList(mutableGoodHabits, it.filter { h -> h.type == HabitType.GOOD })
            changeList(mutableBadHabits, it.filter { h -> h.type == HabitType.BAD })
        }
    }
}