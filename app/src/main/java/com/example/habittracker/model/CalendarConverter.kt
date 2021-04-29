package com.example.habittracker.model

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class CalendarConverter {
    @TypeConverter
    fun fromCalendar(date: Calendar) = SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss",
        Locale.getDefault()
    ).format(date.time)

    @TypeConverter
    fun toCalendar(data: String): Calendar {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val d = sdf.parse(data)
        val cal = Calendar.getInstance()
        cal.time = d
        return cal
    }
}