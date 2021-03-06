package com.example.domain

import android.graphics.Color
import com.example.domain.test.HabitsListForTest
import com.example.domain.test.TestHabitsRepository
import com.example.domain.usecases.EditHabitUseCase
import com.example.domain.usecases.GetHabitToEditUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*

class UseCasesTest {
    private lateinit var repository: TestHabitsRepository
    private lateinit var habits: List<Habit>

    @Before
    fun setUp() {
        habits = HabitsListForTest.create()
        repository = TestHabitsRepository(habits.toMutableList())
    }

    @Test
    fun getAllHabitsUseCase() = runBlocking {
        val getAllHabitsUseCase = GetAllHabitsUseCase(repository)
        Assert.assertEquals(6, getAllHabitsUseCase.getAllHabits().first().size)
    }

    @Test
    fun insertHabitUseCase() = runBlocking {
        val habitToInsert = Habit(
            "",
            "...",
            HabitPriority.HIGH,
            HabitType.GOOD,
            3 to 7,
            Color.GREEN,
            GregorianCalendar(2021, 5, 12).time.time,
            mutableMapOf(),
            GregorianCalendar(2021, 5, 12).time to GregorianCalendar(2021, 5, 17).time,
            1,
            "111"
        )
        val insertHabitUseCase = InsertHabitUseCase(repository)
        insertHabitUseCase.insertHabit(habitToInsert)
        val h = repository.getAll().first()
        Assert.assertTrue(h.contains(habitToInsert))
        Assert.assertEquals(7, h.size)
    }

    @Test
    fun markHabitAsCompletedUseCase() = runBlocking {
        val markHabitAsCompletedUseCase = MarkHabitAsCompletedUseCase(repository)
        markHabitAsCompletedUseCase.markHabitAsCompleted(habits[0])
        Assert.assertEquals(2, repository.getAll().first()[0].completionsCount)
    }

    @Test
    fun editHabitToRepository() = runBlocking {
        val editHabitUseCase = EditHabitUseCase(repository)
        editHabitUseCase.editHabit(habits[0])
        Assert.assertEquals(habits[0], repository.habitToEdit.value)
    }

    @Test
    fun getHabitToEditUseCase() = runBlocking {
        val getHabitToEditUseCase = GetHabitToEditUseCase(repository)
        editHabitToRepository()
        Assert.assertEquals(habits[0], getHabitToEditUseCase.getHabitToEdit().first())
    }

    @Test
    fun deleteHabitUseCase() = runBlocking {
        val deleteHabitUseCase = DeleteHabitUseCase(repository)
        deleteHabitUseCase.deleteHabit(habits[0])
        val h = repository.getAll().first()
        Assert.assertFalse(h.contains(habits[0]))
        Assert.assertEquals(5, h.size)
    }

    @Test
    fun getHabitsSortedByPriorityAscending() = runBlocking {
        val result = FilterAndSortHabitsUseCase(repository).getHabitsSortedByPriorityAscending()
        val expected = habits.sortedBy { it.priority }
        for (i in 0 until expected.size)
            Assert.assertEquals(expected[i], result[i])
    }

    @Test
    fun getHabitsSortedByPriorityDescending() = runBlocking {
        val result = FilterAndSortHabitsUseCase(repository).getHabitsSortedByPriorityDescending()
        val expected = habits.sortedByDescending { it.priority }
        for (i in 0 until expected.size)
            Assert.assertEquals(expected[i], result[i])
    }

    @Test
    fun getHabitsSortedByNameAscending() = runBlocking {
        val result = FilterAndSortHabitsUseCase(repository).getHabitsSortedByNameAscending()
        val expected = habits.sortedBy { it.name }
        for (i in 0 until expected.size)
            Assert.assertEquals(expected[i], result[i])
    }

    @Test
    fun getHabitsSortedByNameDescending() = runBlocking {
        val result = FilterAndSortHabitsUseCase(repository).getHabitsSortedByNameDescending()
        val expected = habits.sortedByDescending { it.name }
        for (i in 0 until expected.size)
            Assert.assertEquals(expected[i], result[i])
    }

    @Test
    fun getHabitsSortedByChangingDateAscending() = runBlocking {
        val result = FilterAndSortHabitsUseCase(repository).getHabitsSortedByChangingDateAscending()
        val expected = habits.sortedBy { it.changingDate }
        for (i in 0 until expected.size)
            Assert.assertEquals(expected[i], result[i])
    }

    @Test
    fun getHabitsSortedByChangingDateDescending() = runBlocking {
        val result =
            FilterAndSortHabitsUseCase(repository).getHabitsSortedByChangingDateDescending()
        val expected = habits.sortedByDescending { it.changingDate }
        for (i in 0 until expected.size)
            Assert.assertEquals(expected[i], result[i])
    }

    @Test
    fun getHabitsFilteredByName() = runBlocking {
        val result = FilterAndSortHabitsUseCase(repository).getHabitsFilteredByName("??")
        Assert.assertEquals(1, result.size)
        Assert.assertEquals(habits[1], result[0])
    }

    @Test
    fun getHabitsFilteredByNameEmptyString() = runBlocking {
        val result = FilterAndSortHabitsUseCase(repository).getHabitsFilteredByName("")
        Assert.assertEquals(6, result.size)
    }

    @Test
    fun getHabitsFilteredByColor() = runBlocking {
        val colors = listOf(Color.GREEN)
        val result = FilterAndSortHabitsUseCase(repository).getHabitsFilteredByColor(colors)
        Assert.assertEquals(3, result.size)
        Assert.assertTrue(result.containsAll(habits.filter { it.color == Color.GREEN }))
    }

    @Test
    fun getHabitsFilteredByColorIfNoHabitHasSpecificColor() = runBlocking {
        val colors = listOf(Color.GRAY)
        val result = FilterAndSortHabitsUseCase(repository).getHabitsFilteredByColor(colors)
        Assert.assertEquals(0, result.size)
    }

    @Test
    fun getHabitsFilteredByColorSeveralColors() = runBlocking {
        val colors = listOf(Color.GREEN, Color.RED)
        val result = FilterAndSortHabitsUseCase(repository).getHabitsFilteredByColor(colors)
        Assert.assertEquals(6, result.size)
    }

    @Test
    fun getHabitsFilteredByColorSeveralColorWithOneThatNoOneHabitHas() = runBlocking {
        val colors = listOf(Color.GREEN, Color.RED, Color.GRAY)
        val result = FilterAndSortHabitsUseCase(repository).getHabitsFilteredByColor(colors)
        Assert.assertEquals(6, result.size)
    }

    @Test
    fun getHabitsFilteredByColorEmptyColorList() = runBlocking {
        val colors = listOf<Int>()
        val result = FilterAndSortHabitsUseCase(repository).getHabitsFilteredByColor(colors)
        Assert.assertEquals(6, result.size)
    }
}