package com.example.habittracker.habitprogresslinechart

import android.graphics.Color
import com.example.domain.Habit
import com.example.domain.HabitType
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

object HabitProgressLineChart {
    fun draw(lineChartView: LineChart, habit: Habit) {
        val max = habit.previousPeriodToCompletionsCount.size + 2
        lineChartView.xAxis.labelCount = max
        lineChartView.xAxis.valueFormatter = CompletionsDatesValueFormatter(habit)
        val valueFormatter = CompletionsCountValueFormatter()
        val lines = ArrayList<ILineDataSet>()
        lines.add(getCompletedTimesLine(habit, valueFormatter))
        lines.add(getMinimumOrMaximumLine(habit, valueFormatter, max.toFloat()))
        lines.add(getInvisibleLine(habit, InvisibleLineValueFormatter(), max.toFloat()))
        val data = LineData(lines)
        lineChartView.data = data
        lineChartView.axisLeft.valueFormatter = valueFormatter
        lineChartView.axisRight.valueFormatter = valueFormatter
        lineChartView.xAxis.setDrawGridLines(false)
        lineChartView.axisRight.setDrawGridLines(false)
        lineChartView.axisLeft.setDrawGridLines(false)
        lineChartView.axisRight.setDrawLabels(false)
        lineChartView.axisLeft.setDrawLabels(false)
        lineChartView.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChartView.description.isEnabled = false
        lineChartView.xAxis.labelRotationAngle = -45f
    }

    private fun getCompletedTimesLine(habit: Habit, valueFormatter: ValueFormatter): LineDataSet {
        val completedTimes = ArrayList<Entry>()
        var i = 0
        habit.previousPeriodToCompletionsCount.forEach { (_, count) ->
            completedTimes.add(Entry(i.toFloat(), count.toFloat()))
            i++
        }
        completedTimes.add(Entry(i.toFloat(), habit.completionsCount.toFloat()))
        val lineDataSet = LineDataSet(completedTimes, "количество раз")
        lineDataSet.lineWidth = 4f
        lineDataSet.circleRadius = 5f
        lineDataSet.color = habit.color
        lineDataSet.setCircleColor(habit.color)
        lineDataSet.valueFormatter = valueFormatter
        return lineDataSet
    }

    private fun getMinimumOrMaximumLine(
        habit: Habit,
        valueFormatter: ValueFormatter,
        xMax: Float
    ): LineDataSet {
        val minimumOrMaximumLineValues = ArrayList<Entry>()
        minimumOrMaximumLineValues.add(Entry(0f, habit.periodicityTimesPerDay.first.toFloat()))
        minimumOrMaximumLineValues.add(Entry(xMax, habit.periodicityTimesPerDay.first.toFloat()))
        val minimumOrMaximumLine =
            LineDataSet(
                minimumOrMaximumLineValues,
                if (habit.type == HabitType.GOOD) "минимум" else "максимум"
            )
        minimumOrMaximumLine.lineWidth = 4f
        minimumOrMaximumLine.circleRadius = 5f
        val color = if (habit.type == HabitType.GOOD) Color.GREEN else Color.RED
        minimumOrMaximumLine.color = color
        minimumOrMaximumLine.setCircleColor(color)
        minimumOrMaximumLine.valueFormatter = valueFormatter
        return minimumOrMaximumLine
    }

    private fun getInvisibleLine(
        habit: Habit,
        valueFormatter: ValueFormatter,
        xMax: Float
    ): LineDataSet {
        val invisibleLinesEntries = ArrayList<Entry>()
        invisibleLinesEntries.add(Entry(0f, 0f))
        val y = habit.previousPeriodToCompletionsCount.map { it.value }.maxOrNull()
        var yMax =
            if (y == null) habit.completionsCount else if (y > habit.completionsCount) y else habit.completionsCount
        if (yMax == 0) yMax++
        invisibleLinesEntries.add(Entry(xMax, yMax * 2f))
        val invisibleLines = LineDataSet(invisibleLinesEntries, "")
        invisibleLines.color = Color.TRANSPARENT
        invisibleLines.setCircleColor(Color.TRANSPARENT)
        invisibleLines.circleHoleColor = Color.TRANSPARENT
        invisibleLines.valueFormatter = valueFormatter
        return invisibleLines
    }
}