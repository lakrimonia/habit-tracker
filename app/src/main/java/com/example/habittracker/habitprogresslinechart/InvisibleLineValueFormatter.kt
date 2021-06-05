package com.example.habittracker.habitprogresslinechart

import com.github.mikephil.charting.formatter.ValueFormatter

class InvisibleLineValueFormatter : ValueFormatter(){
    override fun getFormattedValue(value: Float): String {
        return ""
    }
}