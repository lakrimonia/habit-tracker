package com.example.habittracker

import android.os.Bundle
import androidx.fragment.app.Fragment

class HabitsListFragment : Fragment() {
    companion object {
        const val TAG = "habits list"
    }

    val habitsList: ArrayList<Habit> by lazy {
        arrayListOf()
    }

    var habitId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }
}