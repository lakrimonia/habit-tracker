package com.example.habittracker.mainpage

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.Habit
import com.example.habittracker.R
import com.example.habittracker.databinding.HabitItemBinding
import com.example.habittracker.habitprogresslinechart.HabitProgressLineChart
import java.text.SimpleDateFormat
import java.util.*

class HabitsAdapter(
    private val habits: MutableList<Habit>,
    private val habitCompletedButtonOnClick: (Habit) -> Unit,
    private val editHabitOnClick: (Habit) -> Unit,
    private val deleteHabitOnClick: (Habit) -> Unit
) : ListAdapter<Habit, HabitsAdapter.HabitsViewHolder>(HabitDiffCallback()) {
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
            binding.habitChangingDate.text = SimpleDateFormat(
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
            HabitProgressLineChart.draw(binding.progress, habit)
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