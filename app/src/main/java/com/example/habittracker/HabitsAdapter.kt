package com.example.habittracker

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.databinding.HabitItemBinding

class HabitsAdapter(
    private val habits: List<Habit>,
    private val onClick: (Habit) -> Unit
) : RecyclerView.Adapter<HabitsAdapter.HabitsViewHolder>() {

    class HabitsViewHolder(
        private val binding: HabitItemBinding,
        private val onClick: (Habit) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private var currentHabit: Habit? = null

        init {
            binding.root.setOnClickListener {
                currentHabit?.let {
                    onClick(it)
                }
            }
        }

        fun bind(habit: Habit) {
            currentHabit = habit
            binding.habitName.text = habit.name
            binding.habitDescription.text = habit.description
            val habitTypeImage = when (habit.type) {
                HabitType.GOOD -> R.drawable.ic_good_24
                HabitType.BAD -> R.drawable.ic_bad_24
            }
            binding.habitType.setImageDrawable(
                (ContextCompat.getDrawable(
                    binding.root.context,
                    habitTypeImage
                ))
            )
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
            binding.root.background = ColorDrawable(habit.color)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitsViewHolder {

        val binding =
            HabitItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HabitsViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: HabitsViewHolder, position: Int) {
        holder.bind(habits[position])
    }

    override fun getItemCount() = habits.size
}