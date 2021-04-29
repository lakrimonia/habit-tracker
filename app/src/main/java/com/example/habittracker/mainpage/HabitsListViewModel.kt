package com.example.habittracker.mainpage

import androidx.lifecycle.*
import com.example.habittracker.Event
import com.example.habittracker.model.Habit
import com.example.habittracker.model.HabitRepository

class HabitsListViewModelFactory(private val repository: HabitRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HabitsListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HabitsListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class HabitsListViewModel(private val repository: HabitRepository) : ViewModel() {
    private val _startToCreateHabit by lazy {
        MutableLiveData<Event<Int>>()
    }
    val startToCreateHabit: LiveData<Event<Int>> = _startToCreateHabit
    val mediatorLiveData: MediatorLiveData<List<Habit>> by lazy {
        MediatorLiveData()
    }
    private val _resetFilters by lazy {
        MutableLiveData<Event<Int>>()
    }
    val resetFilters = _resetFilters
    private val _habits by lazy {
        MutableLiveData<List<Habit>>()
    }
    private val _startToEditHabit by lazy {
        MutableLiveData<Event<Habit>>()
    }
    val startToEditHabit: LiveData<Event<Habit>> = _startToEditHabit

    val habits: LiveData<List<Habit>> = _habits

    private var sortedHabits: List<Habit>? = null
    private var filteredByNameHabits: List<Habit>? = null
    private var filteredByColorHabits: List<Habit>? = null

    val colors: MutableList<Int> by lazy {
        mutableListOf()
    }

    init {
        mediatorLiveData.addSource(repository.allHabits) {
            _habits.value = it
            colors.clear()
            sortedHabits = it
            filteredByNameHabits = it
            filteredByColorHabits = it
        }
    }

    fun clickOnHabitItem(habit: Habit) {
        HabitRepository.setHabitToEdit(habit)
        _startToEditHabit.value = Event(habit)
//        resetFilters()
    }

    fun clickOnFab() {
        _startToCreateHabit.value = Event(0)
//        resetFilters()
    }

    fun sortHabitsByPriorityAscending() {
        _habits.value?.let { habitsList ->
            sortedHabits = sortedHabits?.sortedBy { it.priority }
            _habits.value = sortedHabits?.intersect(habitsList)?.toList()
        }
    }

    fun sortHabitsByPriorityDescending() {
        _habits.value?.let { habitsList ->
            sortedHabits = sortedHabits?.sortedByDescending { it.priority }
            _habits.value = sortedHabits?.intersect(habitsList)?.toList()
        }
    }

    fun sortHabitsByNameAscending() {
        _habits.value?.let { habitsList ->
            sortedHabits = sortedHabits?.sortedBy { it.name }
            _habits.value = sortedHabits?.intersect(habitsList)?.toList()
        }
    }

    fun sortHabitsByNameDescending() {
        _habits.value?.let { habitsList ->
            sortedHabits = sortedHabits?.sortedByDescending { it.name }
            _habits.value = sortedHabits?.intersect(habitsList)?.toList()
        }
    }

    fun sortHabitsByCreatingDateAscending() {
        _habits.value?.let { habitsList ->
            sortedHabits = sortedHabits?.sortedBy { it.creatingDate }
            _habits.value = sortedHabits?.intersect(habitsList)?.toList()
        }
    }

    fun sortHabitsByCreatingDateDescending() {
        _habits.value?.let { habitsList ->
            sortedHabits = sortedHabits?.sortedByDescending { it.creatingDate }
            _habits.value = sortedHabits?.intersect(habitsList)?.toList()
        }
    }

    fun findByName(name: CharSequence) {
        _habits.value?.let {
            filteredByNameHabits =
                repository.allHabits.value?.filter { it.name.startsWith(name, true) }
            _habits.value =
                sortedHabits?.intersect(filteredByColorHabits!!)?.intersect(filteredByNameHabits!!)
                    ?.toList()
        }
    }

    fun findByColor(color: Int) {
        colors.add(color)
        _habits.value?.let {
            repository.allHabits.value?.let { allHabits ->
                filteredByColorHabits = allHabits.filter { colors.contains(it.color) }
                _habits.value =
                    sortedHabits!!.intersect(filteredByNameHabits!!)
                        .intersect(filteredByColorHabits!!)
                        .toList()
            }
        }
    }

    fun removeColor(color: Int) {
        colors.remove(color)
        _habits.value?.let { habitsList ->
            repository.allHabits.value?.let { allHabits ->
                if (colors.isEmpty()) {
                    filteredByColorHabits = allHabits
                    _habits.value = sortedHabits!!.intersect(filteredByNameHabits!!).toList()
                } else {
                    filteredByColorHabits = allHabits.filter { colors.contains(it.color) }
                    _habits.value =
                        habitsList.intersect(filteredByColorHabits!!).toList()
                }
            }
        }
    }

    fun clickOnResetFilters() {
        _resetFilters.value = Event(1)
        resetFilters()
    }

    private fun resetFilters() {
        colors.clear()
        sortedHabits = repository.allHabits.value
        filteredByNameHabits = repository.allHabits.value
        filteredByColorHabits = repository.allHabits.value
        _habits.value = repository.allHabits.value
    }
}