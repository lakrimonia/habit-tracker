package com.example.habittracker.mainpage

import android.graphics.Color
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.domain.*
import com.example.domain.test.HabitsListForTest
import com.example.domain.test.TestHabitsRepository
import com.example.domain.usecases.EditHabitUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HabitsListViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: HabitsListViewModel
    private lateinit var habits: List<Habit>
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        habits = HabitsListForTest.create()
        val repository = TestHabitsRepository(habits.toMutableList())
        viewModel = HabitsListViewModel(
            GetAllHabitsUseCase(repository),
            DeleteHabitUseCase(repository),
            FilterAndSortHabitsUseCase(repository),
            MarkHabitAsCompletedUseCase(repository),
            EditHabitUseCase(repository)
        )
        viewModel.mediatorLiveData.observeForever { }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getGoodHabits() {
        val result = viewModel.goodHabits.value
        val expected = habits.filter { it.type == HabitType.GOOD }
        assertFalse(result == null)
        result?.let {
            assertEquals(3, it.size)
            assertTrue(it.containsAll(expected))
        }
    }

    @Test
    fun getBadHabits() {
        val result = viewModel.badHabits.value
        val expected = habits.filter { it.type == HabitType.BAD }
        assertFalse(result == null)
        result?.let {
            assertEquals(3, it.size)
            assertTrue(it.containsAll(expected))
        }
    }

    @Test
    fun clickOnFab() {
        viewModel.startToCreateHabit.observeForever {}
        viewModel.clickOnFab()
        assertFalse(viewModel.startToCreateHabit.value == null)
        viewModel.startToCreateHabit.value?.let {
            assertFalse(it.getContentIfNotHandled() == null)
        }
    }

    private fun checkListsEquals(expectedGoodHabits: List<Habit>, expectedBadHabits: List<Habit>) {
        assertFalse(viewModel.goodHabits.value == null || viewModel.badHabits.value == null)
        viewModel.goodHabits.value?.let { good ->
            viewModel.badHabits.value?.let { bad ->
                for (i in 0 until 3) {
                    assertTrue(good[i] == expectedGoodHabits[i])
                    assertTrue(bad[i] == expectedBadHabits[i])
                }
            }
        }
    }

    @Test
    fun sortHabitsByPriorityAscending() {
        viewModel.sortHabitsByPriorityAscending()
        val expectedGoodHabits =
            habits.filter { it.type == HabitType.GOOD }.sortedBy { it.priority }
        val expectedBadHabits = habits.filter { it.type == HabitType.BAD }.sortedBy { it.priority }
        checkListsEquals(expectedGoodHabits, expectedBadHabits)
    }

    @Test
    fun sortHabitsByPriorityDescending() {
        viewModel.sortHabitsByPriorityDescending()
        val expectedGoodHabits =
            habits.filter { it.type == HabitType.GOOD }.sortedByDescending { it.priority }
        val expectedBadHabits =
            habits.filter { it.type == HabitType.BAD }.sortedByDescending { it.priority }
        checkListsEquals(expectedGoodHabits, expectedBadHabits)
    }

    @Test
    fun sortHabitsByNameAscending() {
        viewModel.sortHabitsByNameAscending()
        val expectedGoodHabits =
            habits.filter { it.type == HabitType.GOOD }.sortedBy { it.name }
        val expectedBadHabits = habits.filter { it.type == HabitType.BAD }.sortedBy { it.name }
        checkListsEquals(expectedGoodHabits, expectedBadHabits)
    }

    @Test
    fun sortHabitsByNameDescending() {
        viewModel.sortHabitsByNameDescending()
        val expectedGoodHabits =
            habits.filter { it.type == HabitType.GOOD }.sortedByDescending { it.name }
        val expectedBadHabits =
            habits.filter { it.type == HabitType.BAD }.sortedByDescending { it.name }
        checkListsEquals(expectedGoodHabits, expectedBadHabits)
    }

    @Test
    fun sortHabitsByChangingDateAscending() {
        viewModel.sortHabitsByChangingDateAscending()
        val expectedGoodHabits =
            habits.filter { it.type == HabitType.GOOD }.sortedBy { it.changingDate }
        val expectedBadHabits =
            habits.filter { it.type == HabitType.BAD }.sortedBy { it.changingDate }
        checkListsEquals(expectedGoodHabits, expectedBadHabits)
    }

    @Test
    fun sortHabitsByChangingDateDescending() {
        viewModel.sortHabitsByChangingDateDescending()
        val expectedGoodHabits =
            habits.filter { it.type == HabitType.GOOD }.sortedByDescending { it.changingDate }
        val expectedBadHabits =
            habits.filter { it.type == HabitType.BAD }.sortedByDescending { it.changingDate }
        checkListsEquals(expectedGoodHabits, expectedBadHabits)
    }

    @Test
    fun findByNameEmptyString() {
        viewModel.findByName("")
        assertFalse(viewModel.goodHabits.value == null || viewModel.badHabits.value == null)
        viewModel.goodHabits.value?.let { good ->
            viewModel.badHabits.value?.let { bad ->
                assertEquals(3, good.size)
                assertEquals(3, bad.size)
            }
        }
    }

    @Test
    fun findByName() {
        viewModel.findByName("С")
        assertFalse(viewModel.goodHabits.value == null || viewModel.badHabits.value == null)
        viewModel.goodHabits.value?.let { good ->
            viewModel.badHabits.value?.let { bad ->
                assertEquals(1, good.size)
                assertEquals(habits[0], good[0])
                assertEquals(0, bad.size)
            }
        }
    }

    @Test
    fun findByNameZeroResult() {
        viewModel.findByName("Сf")
        assertFalse(viewModel.goodHabits.value == null || viewModel.badHabits.value == null)
        viewModel.goodHabits.value?.let { good ->
            viewModel.badHabits.value?.let { bad ->
                assertEquals(0, good.size)
                assertEquals(0, bad.size)
            }
        }
    }

    @Test
    fun findByColor() {
        viewModel.findByColor(Color.GREEN)
        val expectedGoodHabits =
            habits.filter { (it.type == HabitType.GOOD) && (it.color == Color.GREEN) }
        val expectedBadHabits =
            habits.filter { (it.type == HabitType.BAD) && (it.color == Color.GREEN) }
        assertFalse(viewModel.goodHabits.value == null || viewModel.badHabits.value == null)
        viewModel.goodHabits.value?.let { good ->
            viewModel.badHabits.value?.let { bad ->
                assertEquals(expectedGoodHabits.size, good.size)
                assertTrue(good.containsAll(expectedGoodHabits))
                assertEquals(expectedBadHabits.size, bad.size)
                assertTrue(bad.containsAll(expectedBadHabits))
            }
        }
    }

    @Test
    fun findByColorNoHabitHasSpecificColor() {
        viewModel.findByColor(Color.GRAY)
        assertFalse(viewModel.goodHabits.value == null || viewModel.badHabits.value == null)
        viewModel.goodHabits.value?.let { good ->
            viewModel.badHabits.value?.let { bad ->
                assertEquals(0, good.size)
                assertEquals(0, bad.size)
            }
        }
    }

    @Test
    fun findByColorSeveralColors() {
        viewModel.findByColor(Color.GREEN)
        viewModel.findByColor(Color.RED)
        assertFalse(viewModel.goodHabits.value == null || viewModel.badHabits.value == null)
        viewModel.goodHabits.value?.let { good ->
            viewModel.badHabits.value?.let { bad ->
                assertEquals(3, good.size)
                assertEquals(3, bad.size)
            }
        }
    }

    @Test
    fun findByColorSeveralColorsAndOneThatNoHabitHas() {
        viewModel.findByColor(Color.GREEN)
        viewModel.findByColor(Color.RED)
        viewModel.findByColor(Color.GRAY)
        assertFalse(viewModel.goodHabits.value == null || viewModel.badHabits.value == null)
        viewModel.goodHabits.value?.let { good ->
            viewModel.badHabits.value?.let { bad ->
                assertEquals(3, good.size)
                assertEquals(3, bad.size)
            }
        }
    }

    @Test
    fun removeColor() {
        viewModel.findByColor(Color.GREEN)
        viewModel.findByColor(Color.RED)
        viewModel.removeColor(Color.RED)
        val expectedGoodHabits =
            habits.filter { (it.type == HabitType.GOOD) && (it.color == Color.GREEN) }
        val expectedBadHabits =
            habits.filter { (it.type == HabitType.BAD) && (it.color == Color.GREEN) }
        assertFalse(viewModel.goodHabits.value == null || viewModel.badHabits.value == null)
        viewModel.goodHabits.value?.let { good ->
            viewModel.badHabits.value?.let { bad ->
                assertEquals(expectedGoodHabits.size, good.size)
                assertTrue(good.containsAll(expectedGoodHabits))
                assertEquals(expectedBadHabits.size, bad.size)
                assertTrue(bad.containsAll(expectedBadHabits))
            }
        }
    }

    @Test
    fun removeAllColors() {
        viewModel.findByColor(Color.GREEN)
        viewModel.removeColor(Color.GREEN)
        assertFalse(viewModel.goodHabits.value == null || viewModel.badHabits.value == null)
        viewModel.goodHabits.value?.let { good ->
            viewModel.badHabits.value?.let { bad ->
                assertEquals(3, good.size)
                assertEquals(3, bad.size)
            }
        }
    }

    @Test
    fun clickOnResetFilters() {
        viewModel.resetFilters.observeForever {}
        viewModel.findByColor(Color.GREEN)
        viewModel.findByName("С")
        viewModel.clickOnResetFilters()
        assertFalse(viewModel.resetFilters.value == null)
        viewModel.resetFilters.value?.let {
            assertFalse(it.getContentIfNotHandled() == null)
        }
        assertFalse(viewModel.goodHabits.value == null || viewModel.badHabits.value == null)
        viewModel.goodHabits.value?.let { good ->
            viewModel.badHabits.value?.let { bad ->
                assertEquals(3, good.size)
                assertEquals(3, bad.size)
            }
        }
    }

    @Test
    fun deleteHabitOnClick() {
        viewModel.deleteHabitOnClick(habits[0])
        assertFalse(viewModel.goodHabits.value == null)
        viewModel.goodHabits.value?.let {
            assertFalse(it.contains(habits[0]))
        }
    }

    @Test
    fun editHabitOnClick() {
        viewModel.startToEditHabit.observeForever {}
        viewModel.editHabitOnClick(habits[1])
        assertFalse(viewModel.startToEditHabit.value == null)
        viewModel.startToEditHabit.value?.let {
            assertFalse(it.getContentIfNotHandled() == null)
        }
    }

    @Test
    fun goodHabitWasCompletedLessThanMinimum() {
        viewModel.habitMarkedAsCompleted.observeForever {}
        viewModel.markHabitAsCompletedOnClick(habits[0])
        assertFalse(viewModel.habitMarkedAsCompleted.value == null)
        viewModel.habitMarkedAsCompleted.value?.let {
            val result = it.getContentIfNotHandled()
            assertFalse(result == null)
            result?.let { textToCompletionsCount ->
                assertEquals("Стоит выполнить ещё ", textToCompletionsCount.first)
                assertEquals(1, textToCompletionsCount.second)
            }
        }
    }

    @Test
    fun goodHabitWasCompletedExactlyMinimum() {
        viewModel.habitMarkedAsCompleted.observeForever {}
        viewModel.markHabitAsCompletedOnClick(habits[2])
        assertFalse(viewModel.habitMarkedAsCompleted.value == null)
        viewModel.habitMarkedAsCompleted.value?.let {
            val result = it.getContentIfNotHandled()
            assertFalse(result == null)
            result?.let { textToCompletionsCount ->
                assertEquals("You're breathtaking!", textToCompletionsCount.first)
                assertTrue(textToCompletionsCount.second == null)
            }
        }
    }

    @Test
    fun goodHabitWasCompletedExactlyOrMoreThanMinimum() {
        viewModel.habitMarkedAsCompleted.observeForever {}
        viewModel.markHabitAsCompletedOnClick(habits[1])
        assertFalse(viewModel.habitMarkedAsCompleted.value == null)
        viewModel.habitMarkedAsCompleted.value?.let {
            val result = it.getContentIfNotHandled()
            assertFalse(result == null)
            result?.let { textToCompletionsCount ->
                assertEquals("You're breathtaking!", textToCompletionsCount.first)
                assertTrue(textToCompletionsCount.second == null)
            }
        }
    }

    @Test
    fun badHabitWasCompletedLessThanMaximum() {
        viewModel.habitMarkedAsCompleted.observeForever {}
        viewModel.markHabitAsCompletedOnClick(habits[5])
        assertFalse(viewModel.habitMarkedAsCompleted.value == null)
        viewModel.habitMarkedAsCompleted.value?.let {
            val result = it.getContentIfNotHandled()
            assertFalse(result == null)
            result?.let { textToCompletionsCount ->
                assertEquals("Можете выполнить ещё ", textToCompletionsCount.first)
                assertEquals(1, textToCompletionsCount.second)
            }
        }
    }

    @Test
    fun badHabitWasCompletedExactlyMaximum() {
        viewModel.habitMarkedAsCompleted.observeForever {}
        viewModel.markHabitAsCompletedOnClick(habits[4])
        assertFalse(viewModel.habitMarkedAsCompleted.value == null)
        viewModel.habitMarkedAsCompleted.value?.let {
            val result = it.getContentIfNotHandled()
            assertFalse(result == null)
            result?.let { textToCompletionsCount ->
                assertEquals("Хватит это делать!", textToCompletionsCount.first)
                assertTrue(textToCompletionsCount.second==null)
            }
        }
    }

    @Test
    fun badHabitWasCompletedMoreThanMaximum() {
        viewModel.habitMarkedAsCompleted.observeForever {}
        viewModel.markHabitAsCompletedOnClick(habits[3])
        assertFalse(viewModel.habitMarkedAsCompleted.value == null)
        viewModel.habitMarkedAsCompleted.value?.let {
            val result = it.getContentIfNotHandled()
            assertFalse(result == null)
            result?.let { textToCompletionsCount ->
                assertEquals("Хватит это делать!", textToCompletionsCount.first)
                assertTrue(textToCompletionsCount.second==null)
            }
        }
    }
}