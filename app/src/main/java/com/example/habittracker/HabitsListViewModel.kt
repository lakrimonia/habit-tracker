package com.example.habittracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.collections.ArrayList

class HabitsListViewModel() : ViewModel() {
    private val _habits by lazy {
        MutableLiveData<ArrayList<Habit>>()
    }
    private val _startToCreateHabit by lazy {
        MutableLiveData<Event<Int>>()
    }
    private val _startToEditHabit by lazy {
        MutableLiveData<Event<Habit>>()
    }
//    private val _isFiltered by lazy {
//        MutableLiveData<Boolean>()
//    }
    private val _resetFilters by lazy{
        MutableLiveData<Event<Int>>()
}
    val startToCreateHabit: LiveData<Event<Int>> = _startToCreateHabit
    val startToEditHabit: LiveData<Event<Habit>> = _startToEditHabit
    val habits: LiveData<ArrayList<Habit>> = _habits
    private var sortedHabits: ArrayList<Habit>? = null
    private var filteredByNameHabits: ArrayList<Habit>? = null
    private var filteredByColorHabits: ArrayList<Habit>? = null
//    val isFiltered: LiveData<Boolean> = _isFiltered
    val resetFilters = _resetFilters

    val colors: MutableList<Int> by lazy {
        mutableListOf()
    }

    fun clickOnHabitItem(habit: Habit) {
        _startToEditHabit.value = Event(habit)
        resetFilters()
    }

    fun clickOnFab() {
        _startToCreateHabit.value = Event(0)
        resetFilters()
    }

    fun clickOnResetFilters(){
        _resetFilters.value = Event(1)
        resetFilters()
    }

    fun updateHabitsList() {
        _habits.value = ArrayList(
            (sortedHabits ?: HabitsList.habits).intersect(
                filteredByNameHabits ?: HabitsList.habits
            ).intersect(filteredByColorHabits ?: HabitsList.habits)
        )
    }

    init {
        loadHabits()
    }

    private fun loadHabits() {
        _habits.postValue(HabitsList.habits)
    }

    fun sortHabitsByPriorityAscending() {
//        _isFiltered.value = true
        _habits.value?.let { habitsList ->
            sortedHabits = ArrayList((sortedHabits ?: HabitsList.habits).sortedBy { it.priority })
            _habits.value = ArrayList(sortedHabits?.intersect(habitsList))
        }
    }

    fun sortHabitsByPriorityDescending() {
//        _isFiltered.value = true
        _habits.value?.let { habitsList ->
            sortedHabits =
                ArrayList((sortedHabits ?: HabitsList.habits).sortedByDescending { it.priority })
            _habits.value = ArrayList(sortedHabits?.intersect(habitsList))
        }
    }

    fun sortHabitsByNameAscending() {
//        _isFiltered.value = true
//        _habits.value = ArrayList(_habits.value?.sortedBy { it.name })
        _habits.value?.let { habitsList ->
            sortedHabits =
                ArrayList((sortedHabits ?: HabitsList.habits).sortedBy { it.name })
            _habits.value = ArrayList(sortedHabits?.intersect(habitsList))
        }
    }

    fun sortHabitsByNameDescending() {
//        _isFiltered.value = true
//        _habits.value = ArrayList(_habits.value?.sortedByDescending { it.name })
        _habits.value?.let { habitsList ->
            sortedHabits =
                ArrayList((sortedHabits ?: HabitsList.habits).sortedByDescending { it.name })
            _habits.value = ArrayList(sortedHabits?.intersect(habitsList))
        }
    }

    fun sortHabitsByCreatingDateAscending() {
//        _isFiltered.value = true
//        _habits.value = ArrayList(_habits.value?.sortedBy { it.creatingDate })
        _habits.value?.let { habitsList ->
            sortedHabits =
                ArrayList((sortedHabits ?: HabitsList.habits).sortedBy { it.creatingDate })
            _habits.value = ArrayList(sortedHabits?.intersect(habitsList))
        }
    }

    fun sortHabitsByCreatingDateDescending() {
//        _isFiltered.value = true
//        _habits.value = ArrayList(_habits.value?.sortedByDescending { it.creatingDate })
        _habits.value?.let { habitsList ->
            sortedHabits =
                ArrayList(
                    (sortedHabits ?: HabitsList.habits).sortedByDescending { it.creatingDate })
            _habits.value = ArrayList(sortedHabits?.intersect(habitsList))
        }
    }

    fun findByName(name: CharSequence) {
        if (name.toString() == "")
            return
//        _isFiltered.value = true
//        _habits.value = ArrayList(_habits.value?.filter { it.name.startsWith(name, true) })
        _habits.value?.let { habitsList ->
//            filteredHabits =
//                ArrayList((filteredHabits ?: habitsList).filter { it.name.startsWith(name, true) })
//            _habits.value = filteredHabits

//            filteredByNameHabits = ArrayList((filteredByNameHabits ?: sortedHabits ?: habitsList).filter {
//                it.name.startsWith(
//                    name,
//                    true
//                )
//            })
            filteredByNameHabits =
                ArrayList(HabitsList.habits.filter { it.name.startsWith(name, true) })
            _habits.value = ArrayList(habitsList.intersect(filteredByNameHabits ?: habitsList))
        }
    }

    fun findByColor(color: Int) {
//        _isFiltered.value = true
        colors.add(color)
//        _habits.value = ArrayList(_habits.value?.filter { colors.contains(it.color) })
        _habits.value?.let { habitsList ->
//            filteredHabits =
//                ArrayList((filteredHabits ?: habitsList).filter { colors.contains(it.color) })
//            _habits.value = filteredHabits
//            filteredByColorHabits = ArrayList(HabitsList.habits.intersect())
            filteredByColorHabits =
                ArrayList(HabitsList.habits.filter { colors.contains(it.color) })
            _habits.value = ArrayList(
                (sortedHabits ?: filteredByNameHabits ?: HabitsList.habits).intersect(
                    filteredByNameHabits ?: HabitsList.habits
                ).intersect(filteredByColorHabits ?: HabitsList.habits)
            )
        }
    }

    fun removeColor(color: Int) {
        colors.remove(color)
//        if (colors.isEmpty())
//            _habits.value = HabitsList.habits
//        else _habits.value = ArrayList(_habits.value?.filter { colors.contains(it.color) })
        _habits.value?.let { habitsList ->
            if (colors.isEmpty()) {
                filteredByColorHabits = null
                _habits.value = ArrayList(
                    (sortedHabits ?: filteredByNameHabits ?: HabitsList.habits).intersect(
                        filteredByNameHabits ?: HabitsList.habits
                    )
                )
            } else {
                filteredByColorHabits =
                    ArrayList(HabitsList.habits.filter { colors.contains(it.color) })
                _habits.value = ArrayList(habitsList.intersect(filteredByColorHabits ?: habitsList))
            }
        }
    }

    private fun resetFilters() {
        colors.clear()
        sortedHabits = null
        filteredByNameHabits = null
        filteredByColorHabits = null
        _habits.value = HabitsList.habits
//        _isFiltered.value = false
    }
}