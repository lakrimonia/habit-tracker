package com.example.habittracker.model

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class CalendarConverter {
    @TypeConverter
    fun fromCalendar(date: Calendar): String = SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss",
        Locale.getDefault()
    ).format(date.time)

    @TypeConverter
    fun toCalendar(data: String): Calendar {
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(data)
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar
    }
}