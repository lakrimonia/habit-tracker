package com.example.data

import com.example.domain.Habit
import com.example.domain.HabitPriority
import com.example.domain.HabitType
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.util.*

class HabitJsonDeserializer : JsonDeserializer<Habit> {
    private lateinit var start: Date
    private lateinit var end: Date
    private var completionsCount = 0

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Habit {
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

        val previousPeriodToCompletionsCount = createPeriodToCompletionsCount(doneDates, days)

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
            start to end,
            completionsCount,
            id
        )
    }

    private fun createPeriodToCompletionsCount(
        doneDates: MutableList<Long>,
        days: Int
    ): MutableMap<Pair<Date, Date>, Int> {
        val today = Calendar.getInstance()
        val previousPeriodToCompletionsCount = mutableMapOf<Pair<Date, Date>, Int>()
        if (doneDates.isEmpty()) {
            start = today.time
            today.add(Calendar.DATE, days - 1)
            end = today.time
        } else {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = doneDates[0]
            start = calendar.time
            calendar.add(Calendar.DATE, days - 1)
            end = calendar.time
            completionsCount++
            for (i in 1 until doneDates.size) {
                val cur = Date(doneDates[i])
                if (end.after(cur))
                    completionsCount++
                else {
                    previousPeriodToCompletionsCount[start to end] = completionsCount
                    calendar.add(Calendar.DATE, 1)
                    start = calendar.time
                    calendar.add(Calendar.DATE, days - 1)
                    end = calendar.time
                    completionsCount = 0
                }
            }
        }
        return previousPeriodToCompletionsCount
    }
}