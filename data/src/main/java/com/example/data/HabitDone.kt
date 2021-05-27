package com.example.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class HabitDone(
    val date: Long, @SerializedName("habit_uid")
    val uid: String
) : Serializable