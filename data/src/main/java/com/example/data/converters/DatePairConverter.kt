package com.example.data.converters

import androidx.room.TypeConverter
import java.util.*

class DatePairConverter {
    @TypeConverter
    fun fromPair(pair: Pair<Date, Date>): String = "${pair.first.time},${pair.second.time}"

    @TypeConverter
    fun toPair(data: String): Pair<Date, Date> {
        val t = data.split(",").map { x -> Date(x.toLong()) }
        return t[0] to t[1]
    }
}