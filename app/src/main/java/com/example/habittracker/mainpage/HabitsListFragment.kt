package com.example.habittracker.mainpage

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.habittracker.MainActivityCallback
import com.example.habittracker.databinding.FragmentHabitsListBinding
import com.example.habittracker.model.Habit
import com.example.habittracker.model.HabitDatabase
import com.example.habittracker.model.HabitRepository
import com.example.habittracker.model.HabitType


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
    private lateinit var habitType: HabitType
    private lateinit var viewModel: HabitsListViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = activity as MainActivityCallback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getSerializable(HABIT_TYPE)?.let {
            habitType = it as HabitType
        }

        viewModel = ViewModelProvider(
            requireActivity(),
            HabitsListViewModelFactory(
                HabitRepository(
                    HabitDatabase.getDatabase(requireContext()).habitDao()
                )
            )
        ).get(HabitsListViewModel::class.java)
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
        when (habitType) {
            HabitType.GOOD -> viewModel.goodHabits.observe(viewLifecycleOwner, { habits ->
                if (binding.recyclerView.adapter == null)
                    binding.recyclerView.adapter = HabitsAdapter(habits) { habitItemOnClick(it) }
                binding.recyclerView.adapter?.notifyDataSetChanged()
            })
            HabitType.BAD -> viewModel.badHabits.observe(viewLifecycleOwner, { habits ->
                if (binding.recyclerView.adapter == null)
                    binding.recyclerView.adapter = HabitsAdapter(habits) { habitItemOnClick(it) }
                binding.recyclerView.adapter?.notifyDataSetChanged()
            })
        }

        viewModel.startToEditHabit.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {
                callback?.editHabit()
            }
        })
    }

    private fun habitItemOnClick(habit: Habit) {
        viewModel.clickOnHabitItem(habit)
    }
}