package com.example.habittracker.model

import androidx.room.TypeConverter

class PairConverter {
    @TypeConverter
    fun fromPair(pair: Pair<Int, Int>): String = "${pair.first},${pair.second}"

    @TypeConverter
    fun toPair(data: String): Pair<Int, Int> {
        val t = data.split(",").map { x -> x.toInt() }
        return t[0] to t[1]
    }
}