package com.example.data

import android.content.Context
import android.graphics.Color
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.domain.HabitPriority
import com.example.domain.HabitType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class HabitDatabaseTest {
    private lateinit var habitDao: HabitDao
    private lateinit var habitDb: HabitDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        habitDb = Room.inMemoryDatabaseBuilder(context, HabitDatabase::class.java).build()
        habitDao = habitDb.habitDao()
    }

    @After
    fun closeDb() {
        habitDb.close()
    }

    @Test
    fun dbOrderedCorrectly() = runBlocking{
        val medium = HabitEntity(
            "",
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
        val low = HabitEntity(
            "",
            "",
            HabitPriority.LOW,
            HabitType.GOOD,
            1 to 1,
            Color.GREEN,
            Calendar.getInstance().time.time,
            mutableMapOf(),
            Calendar.getInstance().time to Calendar.getInstance().time,
            0,
            "1"
        )
        val high = HabitEntity(
            "",
            "",
            HabitPriority.HIGH,
            HabitType.GOOD,
            1 to 1,
            Color.GREEN,
            Calendar.getInstance().time.time,
            mutableMapOf(),
            Calendar.getInstance().time to Calendar.getInstance().time,
            0,
            "2"
        )
        habitDao.insert(medium)
        habitDao.insert(low)
        habitDao.insert(high)
        val habits = habitDao.getAll().first()
        assertEquals(HabitPriority.HIGH, habits[0].priority)
        assertEquals(HabitPriority.MEDIUM, habits[1].priority)
        assertEquals(HabitPriority.LOW, habits[2].priority)
    }

    @Test
    fun simpleInsert() = runBlocking {
        habitDao.insert(
            HabitEntity(
                "",
                "",
                HabitPriority.HIGH,
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
        val g = habitDao.getAll().first().size
        assertEquals(1, g)
    }

    @Test
    fun replaceWhenHabitWithSameIdInserted() = runBlocking {
        habitDao.insert(
            HabitEntity(
                "1",
                "",
                HabitPriority.HIGH,
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
        habitDao.insert(
            HabitEntity(
                "2",
                "",
                HabitPriority.HIGH,
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
        val habits = habitDao.getAll().first()
        assertEquals(1, habits.size)
        assertEquals("2", habits[0].name)
    }

    @Test
    fun delete() = runBlocking {
        val habit = HabitEntity(
            "1",
            "",
            HabitPriority.HIGH,
            HabitType.GOOD,
            1 to 1,
            Color.GREEN,
            Calendar.getInstance().time.time,
            mutableMapOf(),
            Calendar.getInstance().time to Calendar.getInstance().time,
            0,
            "0"
        )
        habitDao.insert(habit)
        habitDao.delete(habit)
        assertEquals(0, habitDao.getAll().first().size)
    }

    @Test
    fun clear() = runBlocking {
        habitDao.insert(
            HabitEntity(
                "1",
                "",
                HabitPriority.HIGH,
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
        habitDao.clear()
        assertEquals(0, habitDao.getAll().first().size)
    }

    @Test
    fun getHabitById() = runBlocking {
        habitDao.insert(
            HabitEntity(
                "1",
                "",
                HabitPriority.HIGH,
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
        habitDao.insert(
            HabitEntity(
                "2",
                "",
                HabitPriority.HIGH,
                HabitType.GOOD,
                1 to 1,
                Color.GREEN,
                Calendar.getInstance().time.time,
                mutableMapOf(),
                Calendar.getInstance().time to Calendar.getInstance().time,
                0,
                "1"
            )
        )
        assertEquals("2", habitDao.getHabitById("1").name)
        assertEquals("1", habitDao.getHabitById("0").name)
    }

    @Test
    fun update() = runBlocking {
        habitDao.insert(
            HabitEntity(
                "1",
                "",
                HabitPriority.HIGH,
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
        habitDao.update(
            HabitEntity(
                "123",
                "",
                HabitPriority.HIGH,
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
        val habits = habitDao.getAll().first()
        assertEquals(1, habits.size)
        assertEquals("123", habits[0].name)
    }
}