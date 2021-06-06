package com.example.data

import com.example.domain.Habit
import com.example.domain.HabitPriority
import com.example.domain.HabitType
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.joda.time.DateTimeComparator
import java.lang.reflect.Type
import java.util.*

class HabitJsonDeserializer : JsonDeserializer<Habit> {
    private var start: Date? = null
    private var end: Date? = null
    private var completionsCount = 0

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Habit {
        start = null
        end = null
        completionsCount = 0
        val name = json.asJsonObject.get("title").asString
        val description = json.asJsonObject.get("description").asString
        val priority = enumValues<HabitPriority>()[json.asJsonObject.get("priority").asInt]
        val type = enumValues<HabitType>()[json.asJsonObject.get("type").asInt]
        val times = json.asJsonObject.get("frequency").asInt
        val days = json.asJsonObject.get("count").asInt
        val color = json.asJsonObject.get("color").asInt
        val changingDate = json.asJsonObject.get("date").asLong
        val doneDates =
            json.asJsonObject.get("done_dates").asJsonArray.map { it.asLong }.toMutableList()
        val previousPeriodToCompletionsCount = getPeriodToCompletionsCount(doneDates, days)
        val id = json.asJsonObject.get("uid").asString
        return Habit(
            name,
            description,
            priority,
            type,
            times to days,
            color,
            changingDate,
            previousPeriodToCompletionsCount,
            start!! to end!!,
            completionsCount,
            id
        )
    }

    private fun getPeriodToCompletionsCount(
        doneDates: MutableList<Long>,
        days: Int
    ): MutableMap<Pair<Date, Date>, Int> {
        val comparator = DateTimeComparator.getDateOnlyInstance()
        val today = Calendar.getInstance()
        val previousPeriodToCompletionsCount = mutableMapOf<Pair<Date, Date>, Int>()
        if (doneDates.isEmpty()) {
            start = today.time
            today.add(Calendar.DATE, days - 1)
            end = today.time
        } else {
            val date = Calendar.getInstance()
            date.time = Date(doneDates[0])
            start = date.time
            date.add(Calendar.DATE, days - 1)
            end = date.time
            completionsCount++
            for (i in 1 until doneDates.size) {
                val doneDate = Calendar.getInstance()
                doneDate.time = Date(doneDates[i])
                while (comparator.compare(date.time, doneDate.time) < 0)
                    addDaysWhenHabitCompletedZeroTimes(
                        date,
                        previousPeriodToCompletionsCount,
                        days
                    )
                completionsCount++
            }
            while (comparator.compare(date.time, today.time) < 0)
                addDaysWhenHabitCompletedZeroTimes(date, previousPeriodToCompletionsCount, days)
        }
        return previousPeriodToCompletionsCount
    }

    private fun addDaysWhenHabitCompletedZeroTimes(
        date: Calendar,
        previousPeriodToCompletionsCount: MutableMap<Pair<Date, Date>, Int>,
        days: Int
    ) {
        previousPeriodToCompletionsCount[start!! to end!!] = completionsCount
        date.add(Calendar.DATE, 1)
        start = date.time
        date.add(Calendar.DATE, days - 1)
        end = date.time
        completionsCount = 0
    }
}