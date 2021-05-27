package com.example.data

import com.example.domain.Habit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.PUT

interface HabitApi {
    @GET("habit")
    suspend fun getHabits(): List<Habit>

    @PUT("habit")
    suspend fun addOrUpdateHabit(@Body habit: Habit): HabitUID

    @HTTP(method = "DELETE", path = "habit", hasBody = true)
    suspend fun deleteHabit(@Body habitUID: HabitUID)

    @HTTP(method = "POST", path = "habit_done", hasBody = true)
    suspend fun markHabitCompleted(@Body habitDone: HabitDone)
}

