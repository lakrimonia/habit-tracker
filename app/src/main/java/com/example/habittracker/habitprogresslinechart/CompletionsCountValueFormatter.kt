package com.example.habittracker.habitprogresslinechart

import com.github.mikephil.charting.formatter.ValueFormatter
import kotlin.math.roundToInt

class CompletionsCountValueFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return value.roundToInt().toString()
    }
}