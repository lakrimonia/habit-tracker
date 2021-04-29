package com.example.habittracker.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HabitDao {
    @Query("SELECT * FROM habit_table ORDER BY priority ASC")
    fun getAll(): LiveData<List<Habit>>

    @Query("SELECT COUNT(*) FROM habit_table")
    fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(habit: Habit)

    @Query("DELETE FROM habit_table")
    fun clear()
}