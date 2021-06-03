package com.example.data

import android.content.Context
import android.graphics.Color
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.domain.Habit
import com.example.domain.HabitPriority
import com.example.domain.HabitType
import org.junit.Before
import org.junit.runner.RunWith
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import java.util.*
import org.junit.Assert.*
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
class HabitsRepositoryTest {
    private lateinit var habitDao: HabitDao
    private lateinit var habitDb: HabitDatabase
    private lateinit var habitApi: HabitApi
    private lateinit var habitsRepository: HabitRepository
    private lateinit var habits: MutableList<Habit>

    @Before
    fun createRepository() {
        habits = mutableListOf(
            Habit(
                "0",
                "",
                HabitPriority.MEDIUM,
                HabitType.GOOD,
                1 to 1,
                Color.GREEN,
                Calendar.getInstance().time.time,
                mutableMapOf(),
                Calendar.getInstance().time to Calendar.getInstance().time,
                0,
                "0"
            )
        )
        val context = ApplicationProvider.getApplicationContext<Context>()
        habitDb = Room.inMemoryDatabaseBuilder(context, HabitDatabase::class.java).build()
        habitDao = habitDb.habitDao()
        habitApi = mock(HabitApi::class.java)
        runBlocking {
            `when`(habitApi.getHabits()).thenReturn(habits.toList())
        }
        habitsRepository = HabitRepository(habitDao, habitApi)
    }

    @After
    fun tearDown() {
        habitDb.close()
    }

    @Test
    fun getAll() = runBlocking {
        val h = habitsRepository.getAll().first()
        assertEquals(1, h.size)
    }

    @Test
    fun insert() = runBlocking {
        val habitToInsert = Habit(
            "1",
            "",
            HabitPriority.MEDIUM,
            HabitType.GOOD,
            1 to 1,
            Color.GREEN,
            Calendar.getInstance().time.time,
            mutableMapOf(),
            Calendar.getInstance().time to Calendar.getInstance().time,
            0,
            "1"
        )
        `when`(habitApi.addOrUpdateHabit(habitToInsert)).then {
            habits.add(habitToInsert)
            HabitUID(habitToInsert.id)
        }
        habitsRepository.insert(habitToInsert)
        val h = habitsRepository.getAll().first()
        assertEquals(2, h.size)
    }

    @Test
    fun insertHabitWithAlreadyExistingId() = runBlocking {
        val habitToInsert = Habit(
            "000",
            "",
            HabitPriority.MEDIUM,
            HabitType.GOOD,
            1 to 1,
            Color.GREEN,
            Calendar.getInstance().time.time,
            mutableMapOf(),
            Calendar.getInstance().time to Calendar.getInstance().time,
            0,
            "0"
        )
        `when`(habitApi.addOrUpdateHabit(habitToInsert)).then {
            habits.add(habitToInsert)
            HabitUID(habitToInsert.id)
        }
        habitsRepository.insert(habitToInsert)
        val h = habitsRepository.getAll().first()
        assertEquals(1, h.size)
        assertEquals(habitToInsert, h[0])
    }

    @Test
    fun delete() = runBlocking {
        val habitToDelete = habits[0]
        `when`(habitApi.deleteHabit(HabitUID(habitToDelete.id))).then {
            habits.remove(habitToDelete)
        }
        habitsRepository.delete(habitToDelete)
        val h = habitsRepository.getAll().first()
        assertEquals(0, h.size)
    }

    @Test
    fun markAsCompleted() = runBlocking {
        val habitToMark = habits[0]
        `when`(
            habitApi.markHabitCompleted(
                HabitDone(
                    Calendar.getInstance().time.time,
                    habitToMark.id
                )
            )
        ).then {
            habitToMark.completionsCount++
        }
        habitsRepository.markHabitAsCompleted(habitToMark)
        val h = habitsRepository.getAll().first()
        assertEquals(1, h[0].completionsCount)
    }
}