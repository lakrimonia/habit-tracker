package com.example.habittracker

enum class HabitPriority {
    HIGH, MEDIUM, LOW;

    companion object {
        fun parse(s: String) =
            when (s) {
                "Высокий" -> HIGH
                "Средний" -> MEDIUM
                "Низкий" -> LOW
                else -> throw Exception()
            }
    }
}