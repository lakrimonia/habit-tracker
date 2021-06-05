package com.example.habittracker.habitprogresslinechart

import com.example.domain.Habit
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class CompletionsDatesValueFormatter(private val habit: Habit): ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        if (value % 1.0 != 0.0)
            return ""
        val index = value.roundToInt()
        if (index > habit.previousPeriodToCompletionsCount.size) {
            return ""
        }
        var o = 0
        val dateFormat = SimpleDateFormat(
            "dd.MM.yy",
            Locale.getDefault()
        )
        var start = habit.currentPeriod.first
        run search@{
            habit.previousPeriodToCompletionsCount.forEach {
                if (index == o) {
                    start = it.key.first
                    return@search
                }
                o++
            }
        }
        return dateFormat.format(start)
    }
}