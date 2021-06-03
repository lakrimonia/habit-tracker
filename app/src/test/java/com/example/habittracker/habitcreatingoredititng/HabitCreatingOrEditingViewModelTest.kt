package com.example.habittracker.habitcreatingoredititng

import android.graphics.Color
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.example.domain.HabitPriority
import com.example.domain.HabitType
import com.example.domain.InsertHabitUseCase
import com.example.domain.test.TestHabitsRepository
import com.example.domain.usecases.GetHabitToEditUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule

@ExperimentalCoroutinesApi
class HabitCreatingOrEditingViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: HabitCreatingOrEditingViewModel
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        val repository = TestHabitsRepository(mutableListOf())
        viewModel = HabitCreatingOrEditingViewModel(
            InsertHabitUseCase(repository),
            GetHabitToEditUseCase(repository)
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun setName() {
        viewModel.name.observeForever {}
        viewModel.setName("test")
        viewModel.setValues()
        assertFalse(viewModel.name.value == null)
        viewModel.name.value?.let {
            assertEquals("test", it)
        }
    }

    @Test
    fun setDescription() {
        viewModel.description.observeForever {}
        viewModel.setDescription("test")
        viewModel.setValues()
        assertFalse(viewModel.description.value == null)
        viewModel.description.value?.let {
            assertEquals("test", it)
        }
    }

    @Test
    fun setPriority() {
        viewModel.priority.observeForever {}
        viewModel.setPriority(HabitPriority.MEDIUM)
        viewModel.setValues()
        assertFalse(viewModel.priority.value == null)
        viewModel.priority.value?.let {
            assertEquals(HabitPriority.MEDIUM, it)
        }
    }

    @Test
    fun setType() {
        viewModel.type.observeForever {}
        viewModel.setType(HabitType.BAD)
        viewModel.setValues()
        assertFalse(viewModel.type.value == null)
        viewModel.type.value?.let {
            assertEquals(HabitType.BAD, it)
        }
    }

    @Test
    fun setPeriodicityTimes() {
        viewModel.periodicityTimes.observeForever {}
        viewModel.setPeriodicityTimes("8")
        viewModel.setValues()
        assertFalse(viewModel.periodicityTimes.value == null)
        viewModel.periodicityTimes.value?.let {
            assertEquals(8, it)
        }
    }

    @Test
    fun setPeriodicityDays() {
        viewModel.periodicityDays.observeForever {}
        viewModel.setPeriodicityDays("10")
        viewModel.setValues()
        assertFalse(viewModel.periodicityDays.value == null)
        viewModel.periodicityDays.value?.let {
            assertEquals(10, it)
        }
    }

    @Test
    fun setColor() {
        viewModel.color.observeForever {}
        viewModel.setColor(Color.GREEN.toString())
        viewModel.setValues()
        assertFalse(viewModel.color.value == null)
        viewModel.color.value?.let {
            assertEquals(Color.GREEN, it)
        }

    }

    @Test
    fun clearFields() {
        setName()
        setDescription()
        setPriority()
        setType()
        setPeriodicityTimes()
        setPeriodicityDays()
        setColor()
        viewModel.clearFields()
        viewModel.name.value?.let { assertEquals("", it) }
        viewModel.description.value?.let { assertEquals("", it) }
        viewModel.priority.value?.let { assertEquals(HabitPriority.HIGH, it) }
        viewModel.type.value?.let { assertEquals(HabitType.GOOD, it) }
        assertTrue(viewModel.periodicityTimes.value == null)
        assertTrue(viewModel.periodicityDays.value == null)
    }

    @Test
    fun clickOnFab() {
        setName()
        setDescription()
        setPriority()
        setType()
        setPeriodicityTimes()
        setPeriodicityDays()
        setColor()
        viewModel.saveChanges.observeForever {}
        viewModel.clickOnFab()
        assertFalse(viewModel.saveChanges.value == null)
        viewModel.saveChanges.value?.let {
            assertFalse(it.getContentIfNotHandled() == null)
        }
    }

    @Test
    fun clickOnFabWhenAllFieldsAreEmpty() {
        viewModel.saveChanges.observeForever {}
        viewModel.nameNotEntered.observeForever {}
        viewModel.timesNotEntered.observeForever {}
        viewModel.daysNotEntered.observeForever {}
        viewModel.clickOnFab()
        assertTrue(viewModel.saveChanges.value == null)
        assertFalse(viewModel.nameNotEntered.value == null || viewModel.timesNotEntered.value == null || viewModel.daysNotEntered.value == null)
        viewModel.nameNotEntered.value?.let { nameNotEntered ->
            viewModel.timesNotEntered.value?.let { timesNotEntered ->
                viewModel.daysNotEntered.value?.let { daysNotEntered ->
                    assertTrue(nameNotEntered)
                    assertTrue(timesNotEntered)
                    assertTrue(daysNotEntered)
                }
            }
        }
    }

    @Test
    fun clickOnFabWhenOnlyNameTimesAndDaysAreTyped() {
        setName()
        setPeriodicityTimes()
        setPeriodicityDays()
        setColor()
        viewModel.saveChanges.observeForever {}
        viewModel.clickOnFab()
        assertFalse(viewModel.saveChanges.value == null)
        viewModel.saveChanges.value?.let {
            assertFalse(it.getContentIfNotHandled() == null)
        }
    }

    @Test
    fun typeSomethingInNameFieldAfterError() {
        clickOnFabWhenAllFieldsAreEmpty()
        viewModel.setName(".")
        viewModel.nameNotEntered.value?.let {
            assertFalse(it)
        }
    }

    @Test
    fun typeSomethingInTimesFieldAfterError() {
        clickOnFabWhenAllFieldsAreEmpty()
        viewModel.setPeriodicityTimes("1")
        viewModel.timesNotEntered.value?.let {
            assertFalse(it)
        }
    }

    @Test
    fun typeSomethingInDaysFieldAfterError() {
        clickOnFabWhenAllFieldsAreEmpty()
        viewModel.setPeriodicityDays("1")
        viewModel.daysNotEntered.value?.let {
            assertFalse(it)
        }
    }

    @Test
    fun clickOnFabWhenTimesEqualsZero() {
        setName()
        viewModel.periodicityTimes.observeForever {}
        viewModel.setPeriodicityTimes("0")
        setPeriodicityDays()
        viewModel.timesEqualsZero.observeForever {}
        viewModel.clickOnFab()
        assertFalse(viewModel.timesEqualsZero.value == null)
        viewModel.timesEqualsZero.value?.let {
            assertTrue(it)
        }
        viewModel.setPeriodicityTimes("00")
        viewModel.clickOnFab()
        assertFalse(viewModel.timesEqualsZero.value == null)
        viewModel.timesEqualsZero.value?.let {
            assertTrue(it)
        }
        viewModel.setPeriodicityTimes("000")
        viewModel.clickOnFab()
        assertFalse(viewModel.timesEqualsZero.value == null)
        viewModel.timesEqualsZero.value?.let {
            assertTrue(it)
        }
    }

    @Test
    fun clickOnFabWhenDaysEqualsZero() {
        setName()
        setPeriodicityTimes()
        viewModel.periodicityDays.observeForever {}
        viewModel.setPeriodicityTimes("0")
        viewModel.daysEqualsZero.observeForever {}
        viewModel.clickOnFab()
        assertFalse(viewModel.daysEqualsZero.value == null)
        viewModel.daysEqualsZero.value?.let {
            assertTrue(it)
        }
        viewModel.setPeriodicityTimes("00")
        viewModel.clickOnFab()
        assertFalse(viewModel.daysEqualsZero.value == null)
        viewModel.daysEqualsZero.value?.let {
            assertTrue(it)
        }
        viewModel.setPeriodicityTimes("000")
        viewModel.clickOnFab()
        assertFalse(viewModel.daysEqualsZero.value == null)
        viewModel.daysEqualsZero.value?.let {
            assertTrue(it)
        }
    }
}