package com.example.data.converters

import androidx.room.TypeConverter
import java.util.*

class MutableMapConverter {
    @TypeConverter
    fun fromMap(mutableMap: MutableMap<Pair<Date, Date>, Int>): String {
        if (mutableMap.isEmpty())
            return ""
        return mutableMap.map { x ->
            val a = x.key.first.time
            val b = x.key.second.time
            "$a-$b:${x.value}"
        }.reduce { a, b -> "$a,$b" }
    }

    @TypeConverter
    fun toMap(data: String): MutableMap<Pair<Date, Date>, Int> {
        if (data == "") return mutableMapOf()
        return data.split(",").map { x ->
            val t = x.split(":")
            val t2 = t[0].split("-")
            (Date(t2[0].toLong()) to Date(t2[1].toLong())) to t[1].toInt()
        }.toMap().toMutableMap()
    }
}