package com.example.habittracker

import com.example.habittracker.model.Habit
import retrofit2.http.*


interface HabitApi {
    @GET("habit")
    suspend fun getHabits(): List<Habit>

    @PUT("habit")
    suspend fun addOrUpdateHabit(@Body habit: Habit): HabitUID
}

data class HabitUID(val uid: String)