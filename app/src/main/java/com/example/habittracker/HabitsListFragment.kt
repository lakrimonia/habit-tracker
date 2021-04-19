package com.example.habittracker

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.habittracker.databinding.FragmentHabitsListBinding


class HabitsListFragment : Fragment() {
    companion object {
        const val HABIT_TYPE = "habit type"

        @JvmStatic
        fun newInstance(habitType: HabitType) = HabitsListFragment().apply {
            arguments = Bundle().apply {
                putSerializable(HABIT_TYPE, habitType)
            }
        }
    }

    private var _binding: FragmentHabitsListBinding? = null
    private val binding get() = _binding!!
    private var callback: MainActivityCallback? = null
    private val viewModel: HabitsListViewModel by activityViewModels()
    private lateinit var habitType: HabitType


    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = activity as MainActivityCallback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getSerializable(HABIT_TYPE)?.let {
            habitType = it as HabitType
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.habits.observe(viewLifecycleOwner, { habits ->
            binding.recyclerView.adapter =
                HabitsAdapter(habits, habitType) { habitItemOnClick(it) }
        })

        viewModel.startToEditHabit.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { habit ->
                callback?.editHabit(habit)
            }
        })
    }

    private fun habitItemOnClick(habit: Habit) {
        viewModel.clickOnHabitItem(habit)
    }
}