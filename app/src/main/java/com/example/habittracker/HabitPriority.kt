package com.example.habittracker

enum class HabitPriority {
    LOW, MEDIUM, HIGH;

    companion object {
        fun parse(s: String) =
            when (s) {
                "Низкий" -> LOW
                "Средний" -> MEDIUM
                "Высокий" -> HIGH
                else -> throw Exception()
            }
    }
}