package com.example.habittracker.habitcreatingoredititng

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habittracker.MainActivityCallback
import com.example.habittracker.R
import com.example.habittracker.databinding.FragmentHabitCreatingOrEditingBinding
import com.example.habittracker.mainpage.ColorPicker
import com.example.habittracker.model.*

class HabitCreatingOrEditingFragment : Fragment() {
    private var _binding: FragmentHabitCreatingOrEditingBinding? = null
    private val binding get() = _binding!!
    private var callback: MainActivityCallback? = null
    private lateinit var viewModel: HabitCreatingOrEditingViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = activity as MainActivityCallback
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val dao = HabitDatabase.getDatabase(requireContext()).habitDao()
                @Suppress("UNCHECKED_CAST")
                return HabitCreatingOrEditingViewModel(HabitRepository(dao)) as T
            }
        }).get(HabitCreatingOrEditingViewModel::class.java)

        _binding = FragmentHabitCreatingOrEditingBinding.inflate(inflater, container, false)
        ColorPicker.create(requireContext(), resources, binding.colorsScroll) {
            val color = it?.background as ColorDrawable
            binding.currentColorIcon.setImageDrawable(color)
            viewModel.setColor(color.color)
        }
        binding.floatingActionButton.setOnClickListener {
            viewModel.clickOnFab()
        }

        viewModel.setValues()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pageTitle.text = resources.getString(R.string.habit_creating_title)

        viewModel.mediatorLiveData.observe(viewLifecycleOwner, { })

        binding.nameFieldEditing.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setName(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding.descriptionFieldEditing.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setDescription(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding.priorityFieldEditing.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.setPriority(
                        when (position) {
                            0 -> HabitPriority.HIGH
                            1 -> HabitPriority.MEDIUM
                            2 -> HabitPriority.LOW
                            else -> HabitPriority.LOW
                        }
                    )
                }
            }

        binding.typeFieldEditing.setOnCheckedChangeListener { group, checkedId ->
            viewModel.setType(
                when (checkedId) {
                    binding.goodHabitButton.id -> HabitType.GOOD
                    binding.badHabitButton.id -> HabitType.BAD
                    else -> HabitType.BAD
                }
            )
        }

        binding.timesFieldEditing.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val times = s.toString()
                if (times != "")
                    viewModel.setPeriodicityTimes(times.toInt())
                else viewModel.setPeriodicityTimes(null)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.daysFieldEditing.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val days = s.toString()
                if (days != "")
                    viewModel.setPeriodicityDays(days.toInt())
                else viewModel.setPeriodicityDays(null)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        viewModel.saveChanges.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {
                callback?.returnToMainPage()
            }
        })

        viewModel.name.observe(viewLifecycleOwner, {
            binding.nameFieldEditing.setText(it ?: "")
        })

        viewModel.description.observe(viewLifecycleOwner, {
            binding.descriptionFieldEditing.setText(it ?: "")
        })

        viewModel.priority.observe(viewLifecycleOwner, {
            binding.priorityFieldEditing.setSelection(
                when (it) {
                    HabitPriority.HIGH -> 0
                    HabitPriority.MEDIUM -> 1
                    HabitPriority.LOW -> 2
                    null -> 0
                }
            )
        })

        viewModel.type.observe(viewLifecycleOwner, {
            binding.typeFieldEditing.check(
                when (it) {
                    HabitType.GOOD -> binding.goodHabitButton.id
                    HabitType.BAD -> binding.badHabitButton.id
                    null -> binding.goodHabitButton.id
                }
            )
        })

        viewModel.periodicityTimes.observe(viewLifecycleOwner, {
            it?.let {
                binding.timesFieldEditing.setText(it.toString())
            }
        })

        viewModel.periodicityDays.observe(viewLifecycleOwner, {
            binding.daysFieldEditing.setText(it?.toString() ?: "")

        })

        viewModel.color.observe(viewLifecycleOwner, {
            if (it != null)
                binding.currentColorIcon.setImageDrawable(ColorDrawable(it))
            else {
                val firstColorIcon = binding.colorsScroll.getChildAt(0) as ImageButton
                binding.currentColorIcon.setImageDrawable(firstColorIcon.background)
                viewModel.setColor((binding.currentColorIcon.drawable as ColorDrawable).color)
            }
        })
    }
}