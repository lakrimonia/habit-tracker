package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habit_table ORDER BY priority DESC")
    fun getAll(): Flow<List<HabitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habit: HabitEntity)

    @Update
    suspend fun update(habit: HabitEntity)

    @Delete
    suspend fun delete(habit: HabitEntity)

    @Query("SELECT * FROM habit_table WHERE id = :id LIMIT 1")
    suspend fun getHabitById(id: String): HabitEntity

    @Query("DELETE FROM habit_table")
    suspend fun clear()
}