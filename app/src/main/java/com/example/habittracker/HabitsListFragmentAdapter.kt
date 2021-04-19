package com.example.habittracker

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import java.util.ArrayList

class HabitsListFragmentAdapter(
    activity: AppCompatActivity
) : FragmentStateAdapter(activity) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        if (position == 0)
            return HabitsListFragment.newInstance(HabitType.GOOD)
        return HabitsListFragment.newInstance(HabitType.BAD)
    }
}