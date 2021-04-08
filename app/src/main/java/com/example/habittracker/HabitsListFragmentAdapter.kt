package com.example.habittracker

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.util.ArrayList

class HabitsListFragmentAdapter(
    activity: AppCompatActivity,
    private val habits: ArrayList<Habit>
) : FragmentStateAdapter(activity) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        if (position == 0) {
            val goodHabits =
                ArrayList(habits.filter { habit -> habit.type == HabitType.GOOD })
            return HabitsListFragment.newInstance(goodHabits)
        }
        val badHabits = ArrayList(habits.filter { habit -> habit.type == HabitType.BAD })
        return HabitsListFragment.newInstance(badHabits)
    }
}