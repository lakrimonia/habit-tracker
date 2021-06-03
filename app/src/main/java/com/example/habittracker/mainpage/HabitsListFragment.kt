package com.example.habittracker.mainpage

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.domain.Habit
import com.example.domain.HabitType
import com.example.habittracker.MainActivityCallback
import com.example.habittracker.R
import com.example.habittracker.databinding.FragmentHabitsListBinding
import com.example.habittracker.ApplicationWithDaggerComponent
import javax.inject.Inject


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

    @Inject
    lateinit var viewModel: HabitsListViewModel

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
        val habitsListComponent =
            (requireActivity().application as ApplicationWithDaggerComponent).applicationComponent
                .getViewModelSubcomponent()
                .create()
        habitsListComponent.inject(this)
        _binding = FragmentHabitsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (habitType) {
            HabitType.GOOD -> viewModel.goodHabits.observe(viewLifecycleOwner, { habits ->
                if (binding.recyclerView.adapter == null)
                    binding.recyclerView.adapter =
                        HabitsAdapter(habits,
                            { markHabitAsCompleted(it) },
                            { editHabit(it) },
                            { deleteHabit(it) })
                binding.recyclerView.adapter?.notifyDataSetChanged()
            })
            HabitType.BAD -> viewModel.badHabits.observe(viewLifecycleOwner, { habits ->
                if (binding.recyclerView.adapter == null)
                    binding.recyclerView.adapter =
                        HabitsAdapter(habits,
                            { markHabitAsCompleted(it) },
                            { editHabit(it) },
                            { deleteHabit(it) })
                binding.recyclerView.adapter?.notifyDataSetChanged()
            })
        }

        viewModel.startToEditHabit.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {
                callback?.editHabit()
            }
        })

        viewModel.habitMarkedAsCompleted.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { textToTimes ->
                var finalText = textToTimes.first
                textToTimes.second?.let { times ->
                    finalText += resources.getQuantityString(
                        R.plurals.periodicity_times,
                        times,
                        times
                    )
                }
                val toast = Toast.makeText(requireContext(), finalText, Toast.LENGTH_SHORT)
                toast.show()
            }
        })
    }

    private fun markHabitAsCompleted(habit: Habit) {
        viewModel.markHabitAsCompletedOnClick(habit)
    }

    private fun editHabit(habit: Habit) {
        viewModel.editHabitOnClick(habit)
    }

    private fun deleteHabit(habit: Habit) {
        viewModel.deleteHabitOnClick(habit)
    }
}