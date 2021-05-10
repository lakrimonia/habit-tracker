package com.example.habittracker.model

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class HabitJsonSerializer : JsonSerializer<Habit> {
    override fun serialize(
        src: Habit,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement = JsonObject().apply {
        addProperty("color", src.color)
        addProperty("count", src.periodicityTimesPerDay.second)
        addProperty("date", src.creatingDate)
        addProperty("description", src.description)
        addProperty("frequency", src.periodicityTimesPerDay.first)
        addProperty("priority", src.priority.ordinal)
        addProperty("title", src.name)
        addProperty("type", src.type.ordinal)
        addProperty("uid", src.id)
    }
}