package com.example.habittracker.mainpage

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.Habit
import com.example.domain.HabitType
import com.example.habittracker.R
import com.example.habittracker.databinding.HabitItemBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class HabitsAdapter(
    private val habits: MutableList<Habit>,
    private val habitCompletedButtonOnClick: (Habit) -> Unit,
    private val editHabitOnClick: (Habit) -> Unit,
    private val deleteHabitOnClick: (Habit) -> Unit
) : RecyclerView.Adapter<HabitsAdapter.HabitsViewHolder>() {
    class HabitsViewHolder(
        private val binding: HabitItemBinding,
        private val habitCompletedButtonOnClick: (Habit) -> Unit,
        private val editHabitOnClick: (Habit) -> Unit,
        private val deleteHabitOnClick: (Habit) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private var currentHabit: Habit? = null

        fun bind(habit: Habit) {
            currentHabit = habit
            binding.habitName.text = habit.name
            binding.habitDescription.text = habit.description
            val periodicityTimes = binding.root.resources.getQuantityString(
                R.plurals.periodicity_times,
                habit.periodicityTimesPerDay.first,
                habit.periodicityTimesPerDay.first
            )
            val periodicityDays = binding.root.resources.getQuantityString(
                R.plurals.periodicity_days,
                habit.periodicityTimesPerDay.second,
                habit.periodicityTimesPerDay.second
            )
            binding.habitPeriodicity.text = binding.root.resources.getString(
                R.string.periodicity,
                periodicityTimes,
                periodicityDays
            )
            binding.habitInformation.background = ColorDrawable(habit.color)
            binding.habitCreatingDate.text = SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).format(habit.changingDate)
            binding.completionsCount.text = binding.root.resources.getString(
                R.string.completions_count,
                habit.completionsCount.toString(),
                habit.periodicityTimesPerDay.first.toString()
            )

            binding.habitInformation.setOnClickListener {
                binding.progress.apply {
                    visibility = if (visibility == View.GONE) View.VISIBLE else View.GONE
                }
            }
            binding.habitCompletedButton.setOnClickListener { habitCompletedButtonOnClick(habit) }
            binding.habitOptions.setOnClickListener { view ->
                val p = PopupMenu(binding.root.context, view)
                p.inflate(R.menu.habit_options_menu)
                p.setOnMenuItemClickListener {
                    if (it.itemId == R.id.edit_habit)
                        editHabitOnClick(habit)
                    if (it.itemId == R.id.delete_habit)
                        deleteHabitOnClick(habit)
                    true
                }
                p.show()
            }
            drawLineChart(habit)
        }

        private fun drawLineChart(habit: Habit) {
            val max = habit.previousPeriodToCompletionsCount.size + 2

            val valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return value.roundToInt().toString()
                }
            }

            val dataSets = ArrayList<ILineDataSet>()
            val completedTimes = ArrayList<Entry>()
            var i = 1
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
            dataSets.add(lineDataSet)

            val minimum = ArrayList<Entry>()
            minimum.add(Entry(0f, habit.periodicityTimesPerDay.first.toFloat()))
            minimum.add(Entry(max.toFloat(), habit.periodicityTimesPerDay.first.toFloat()))
            val lds2 =
                LineDataSet(minimum, if (habit.type == HabitType.GOOD) "минимум" else "максимум")
            lds2.lineWidth = 4f
            lds2.circleRadius = 5f
            val color = if (habit.type == HabitType.GOOD) Color.GREEN else Color.RED
            lds2.color = color
            lds2.setCircleColor(color)
            lds2.valueFormatter = valueFormatter
            dataSets.add(lds2)

            val xLabelsFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val index = value.roundToInt() - 1
                    if (index == -1 || index > habit.previousPeriodToCompletionsCount.size) {
                        return ""
                    }
                    var o = 0
                    val dateFormat = SimpleDateFormat(
                        "dd.MM.yy",
                        Locale.getDefault()
                    )
                    var start = habit.currentPeriod.first
                    var end = habit.currentPeriod.second
                    run search@{
                        habit.previousPeriodToCompletionsCount.forEach {
                            if (index == o) {
                                start = it.key.first
                                end = it.key.second
                                return@search
                            }
                            o++
                        }
                    }
                    return if (start == end) dateFormat.format(start) else "${
                        dateFormat.format(
                            start
                        )
                    }-${dateFormat.format(end)}"
                }
            }

            val data = LineData(dataSets)
            binding.progress.data = data

            binding.progress.axisLeft.valueFormatter = valueFormatter
            binding.progress.axisRight.valueFormatter = valueFormatter
            binding.progress.xAxis.valueFormatter = xLabelsFormatter

            binding.progress.xAxis.setDrawGridLines(false)
            binding.progress.axisRight.setDrawGridLines(false)
            binding.progress.axisLeft.setDrawGridLines(false)
            binding.progress.axisRight.setDrawLabels(false)
            binding.progress.axisLeft.setDrawLabels(false)

            binding.progress.xAxis.position = XAxis.XAxisPosition.BOTTOM

            binding.progress.description.isEnabled = false
            binding.progress.xAxis.labelCount = habit.previousPeriodToCompletionsCount.size + 1
            binding.progress.xAxis.labelRotationAngle = -45f
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitsViewHolder {

        val binding =
            HabitItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HabitsViewHolder(
            binding,
            habitCompletedButtonOnClick,
            editHabitOnClick,
            deleteHabitOnClick
        )
    }

    override fun onBindViewHolder(holder: HabitsViewHolder, position: Int) {
        holder.bind(habits[position])
    }

    override fun getItemCount() = habits.size
}