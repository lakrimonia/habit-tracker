package com.example.habittracker

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.habittracker.databinding.FragmentHabitsListBinding

private const val HABITS_LIST = "habits list"

class MainPageFragment : Fragment() {
    private var _binding: FragmentHabitsListBinding? = null
    private val binding get() = _binding!!
    private var callback: MainActivityCallback? = null

    private lateinit var habits: ArrayList<Habit>


    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = activity as MainActivityCallback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getSerializable(HABITS_LIST)?.let {
            habits = it as ArrayList<Habit>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHabitsListBinding.inflate(inflater, container, false)
        val view = binding.root
        arguments?.getSerializable(HABITS_LIST)?.let {
            habits = it as ArrayList<Habit>
        }
        binding.recyclerView.adapter = HabitsAdapter(habits) { habitItemOnClick(it) }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Habit>("habit")?.let {
            habits.add(it)
            binding.recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    private fun habitItemOnClick(habit: Habit) {
        callback?.editHabit(habit)
    }

    companion object {
        @JvmStatic
        fun newInstance(habits: ArrayList<Habit>) =
            MainPageFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(HABITS_LIST, habits)
                }
            }
    }
}