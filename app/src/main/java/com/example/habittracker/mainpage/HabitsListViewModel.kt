package com.example.habittracker.mainpage

import androidx.lifecycle.*
import com.example.domain.*
import com.example.domain.usecases.EditHabitUseCase
import com.example.habittracker.Event
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitsListViewModel @Inject constructor(
    private val getAllHabitsUseCase: GetAllHabitsUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase,
    private val filterAndSortHabitsUseCase: FilterAndSortHabitsUseCase,
    private val markHabitAsCompletedUseCase: MarkHabitAsCompletedUseCase,
    private val editHabitUseCase: EditHabitUseCase
) :
    ViewModel() {
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
    private val mutableHabitMarkedAsCompleted by lazy {
        MutableLiveData<Event<Pair<String, Int?>>>()
    }
    val habitMarkedAsCompleted: LiveData<Event<Pair<String, Int?>>> = mutableHabitMarkedAsCompleted

    private var sortedHabits: List<Habit>? = null
    private var filteredByNameHabits: List<Habit>? = null
    private var filteredByColorHabits: List<Habit>? = null

    private val colors: MutableList<Int> by lazy {
        mutableListOf()
    }

    private var notFilteredGoodHabits: List<Habit> = listOf()
    private var notFilteredBadHabits: List<Habit> = listOf()

    init {
        mutableGoodHabits.value = mutableListOf()
        mutableBadHabits.value = mutableListOf()
        viewModelScope.launch {
            val allHabits = getAllHabitsUseCase.getAllHabits().asLiveData()
            mediatorLiveData.addSource(allHabits) {
                notFilteredGoodHabits = it.filter { h -> h.type == HabitType.GOOD }
                changeList(mutableGoodHabits, notFilteredGoodHabits)
                notFilteredBadHabits = it.filter { h -> h.type == HabitType.BAD }
                changeList(mutableBadHabits, notFilteredBadHabits)
            }
        }
    }

    fun clickOnFab() {
        mutableStartToCreateHabit.value = Event(0)
    }

    private fun changeList(habits: MutableLiveData<MutableList<Habit>>, items: Collection<Habit>) {
        habits.value?.clear()
        habits.value?.addAll(items)
        habits.value = habits.value
    }

    private fun unionSortingAndFilteringResult(): Collection<Habit> {
        var result = sortedHabits ?: filteredByNameHabits ?: filteredByColorHabits ?: setOf()
        sortedHabits?.let { result = result.intersect(it) }
        filteredByNameHabits?.let { result = result.intersect(it) }
        filteredByColorHabits?.let { result = result.intersect(it) }
        return result
    }

    fun sortHabitsByPriorityAscending() {
        viewModelScope.launch {
            sortedHabits = filterAndSortHabitsUseCase.getHabitsSortedByPriorityAscending()
            val union = unionSortingAndFilteringResult()
            changeList(mutableGoodHabits, union.filter { it.type == HabitType.GOOD })
            changeList(mutableBadHabits, union.filter { it.type == HabitType.BAD })
        }
    }

    fun sortHabitsByPriorityDescending() {
        viewModelScope.launch {
            sortedHabits = filterAndSortHabitsUseCase.getHabitsSortedByPriorityDescending()
            val union = unionSortingAndFilteringResult()
            changeList(mutableGoodHabits, union.filter { it.type == HabitType.GOOD })
            changeList(mutableBadHabits, union.filter { it.type == HabitType.BAD })
        }
    }

    fun sortHabitsByNameAscending() {
        viewModelScope.launch {
            sortedHabits = filterAndSortHabitsUseCase.getHabitsSortedByNameAscending()
            val union = unionSortingAndFilteringResult()
            changeList(mutableGoodHabits, union.filter { it.type == HabitType.GOOD })
            changeList(mutableBadHabits, union.filter { it.type == HabitType.BAD })
        }
    }

    fun sortHabitsByNameDescending() {
        viewModelScope.launch {
            sortedHabits = filterAndSortHabitsUseCase.getHabitsSortedByNameDescending()
            val union = unionSortingAndFilteringResult()
            changeList(mutableGoodHabits, union.filter { it.type == HabitType.GOOD })
            changeList(mutableBadHabits, union.filter { it.type == HabitType.BAD })
        }
    }

    fun sortHabitsByChangingDateAscending() {
        viewModelScope.launch {
            sortedHabits = filterAndSortHabitsUseCase.getHabitsSortedByChangingDateAscending()
            val union = unionSortingAndFilteringResult()
            changeList(mutableGoodHabits, union.filter { it.type == HabitType.GOOD })
            changeList(mutableBadHabits, union.filter { it.type == HabitType.BAD })
        }
    }

    fun sortHabitsByChangingDateDescending() {
        viewModelScope.launch {
            sortedHabits = filterAndSortHabitsUseCase.getHabitsSortedByChangingDateDescending()
            val union = unionSortingAndFilteringResult()
            changeList(mutableGoodHabits, union.filter { it.type == HabitType.GOOD })
            changeList(mutableBadHabits, union.filter { it.type == HabitType.BAD })
        }
    }

    fun findByName(name: CharSequence) {
        viewModelScope.launch {
            if (filteredByNameHabits != null || name.isNotEmpty())
                filteredByNameHabits = filterAndSortHabitsUseCase.getHabitsFilteredByName(name)
            val union = unionSortingAndFilteringResult()
            changeList(mutableGoodHabits, union.filter { it.type == HabitType.GOOD })
            changeList(mutableBadHabits, union.filter { it.type == HabitType.BAD })
        }
    }


    fun findByColor(color: Int) {
        colors.add(color)
        viewModelScope.launch {
            filteredByColorHabits =
                filterAndSortHabitsUseCase.getHabitsFilteredByColor(colors)
            val union = unionSortingAndFilteringResult()
            changeList(mutableGoodHabits, union.filter { it.type == HabitType.GOOD })
            changeList(mutableBadHabits, union.filter { it.type == HabitType.BAD })
        }
    }

    fun removeColor(color: Int) {
        colors.remove(color)
        viewModelScope.launch {
            filteredByColorHabits =
                filterAndSortHabitsUseCase.getHabitsFilteredByColor(colors)
            val union = unionSortingAndFilteringResult()
            changeList(mutableGoodHabits, union.filter { it.type == HabitType.GOOD })
            changeList(mutableBadHabits, union.filter { it.type == HabitType.BAD })
        }
    }

    fun clickOnResetFilters() {
        mutableResetFilters.value = Event(1)
        resetFilters()
    }

    private fun resetFilters() {
        colors.clear()
        sortedHabits = null
        filteredByNameHabits = null
        filteredByColorHabits = null
        changeList(mutableGoodHabits, notFilteredGoodHabits)
        changeList(mutableBadHabits, notFilteredBadHabits)
    }

    fun deleteHabitOnClick(habit: Habit) {
        viewModelScope.launch {
            deleteHabitUseCase.deleteHabit(habit)
        }
    }

    fun editHabitOnClick(habit: Habit) {
        viewModelScope.launch {
            editHabitUseCase.editHabit(habit)
            mutableStartToEditHabit.value = Event(habit)
        }
    }

    fun markHabitAsCompletedOnClick(habit: Habit) {
        viewModelScope.launch {
            markHabitAsCompletedUseCase.markHabitAsCompleted(habit)
            mutableGoodHabits.value = mutableGoodHabits.value
            mutableBadHabits.value = mutableBadHabits.value
            val timesToCompleteLeft = habit.periodicityTimesPerDay.first - habit.completionsCount
            mutableHabitMarkedAsCompleted.value = when (habit.type) {
                HabitType.GOOD -> {
                    if (timesToCompleteLeft > 0)
                        Event("Стоит выполнить ещё " to timesToCompleteLeft)
                    else Event("You're breathtaking!" to null)
                }
                HabitType.BAD -> {
                    if (timesToCompleteLeft > 0)
                        Event("Можете выполнить ещё " to timesToCompleteLeft)
                    else Event("Хватит это делать!" to null)
                }
            }
        }
    }
}