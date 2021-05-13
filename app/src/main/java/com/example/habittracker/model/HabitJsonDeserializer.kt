package com.example.habittracker.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class HabitJsonDeserializer : JsonDeserializer<Habit> {
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
        val id = json.asJsonObject.get("uid").asString
        return Habit(
            name, description, priority, type, times to days, color, changingDate, id
        )
    }
}