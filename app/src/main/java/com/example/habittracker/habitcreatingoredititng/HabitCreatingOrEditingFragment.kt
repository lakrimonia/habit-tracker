package com.example.habittracker.habitcreatingoredititng

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.ViewCompat
import com.example.domain.HabitPriority
import com.example.domain.HabitType
import com.example.habittracker.MainActivityCallback
import com.example.habittracker.R
import com.example.habittracker.databinding.FragmentHabitCreatingOrEditingBinding
import com.example.habittracker.ColorPicker
import com.example.habittracker.HabitTrackerApplication
import javax.inject.Inject

class HabitCreatingOrEditingFragment : Fragment(), OnBackPressedListener {
    private var _binding: FragmentHabitCreatingOrEditingBinding? = null
    private val binding get() = _binding!!
    private var callback: MainActivityCallback? = null
    @Inject
    lateinit var viewModel: HabitCreatingOrEditingViewModel

    override fun onBackPressed() {
        viewModel.clearFields()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = activity as MainActivityCallback
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val habitsListComponent =
            (requireActivity().application as HabitTrackerApplication).applicationComponent
                .habitsListComponent()
                .create()
        habitsListComponent.inject(this)
        _binding = FragmentHabitCreatingOrEditingBinding.inflate(inflater, container, false)
        ColorPicker.create(requireContext(), resources, binding.colorsScroll) {
            val color = it?.background as ColorDrawable
            binding.currentColorIcon.setImageDrawable(color)
            viewModel.setColor(color.color.toString())
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

        binding.typeFieldEditing.setOnCheckedChangeListener { _, checkedId ->
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
                    viewModel.setPeriodicityTimes(times)
                else viewModel.setPeriodicityTimes("")
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.daysFieldEditing.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val days = s.toString()
                if (days != "")
                    viewModel.setPeriodicityDays(days)
                else viewModel.setPeriodicityDays("")
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
                viewModel.setColor((binding.currentColorIcon.drawable as ColorDrawable).color.toString())
            }
        })

        viewModel.nameNotEntered.observe(viewLifecycleOwner, { isNotEntered ->
            if (isNotEntered) {
                changeColor(Color.RED, binding.nameField, binding.nameFieldEditing)
                binding.nameNotEntered.visibility = View.VISIBLE
            } else {
                changeColor(Color.BLACK, binding.nameField, binding.nameFieldEditing)
                binding.nameNotEntered.visibility = View.INVISIBLE
            }
        })

        viewModel.timesNotEntered.observe(viewLifecycleOwner, { isNotEntered ->
            if (isNotEntered) {
                changeColor(
                    Color.RED,
                    binding.periodicityField,
                    binding.timesFieldEditing,
                    binding.periodicityFieldFirstPart
                )
                binding.periodicityTimesNotEntered.visibility = View.VISIBLE
            } else {
                changeColor(
                    Color.BLACK,
                    binding.periodicityField,
                    binding.timesFieldEditing,
                    binding.periodicityFieldFirstPart
                )
                binding.periodicityTimesNotEntered.visibility = View.INVISIBLE
            }
        })

        viewModel.daysNotEntered.observe(viewLifecycleOwner, { isNotEntered ->
            if (isNotEntered) {
                changeColor(
                    Color.RED,
                    binding.periodicityField,
                    binding.daysFieldEditing,
                    binding.periodicityFieldSecondPart
                )
                binding.periodicityDaysNotEntered.visibility = View.VISIBLE
            } else {
                changeColor(
                    Color.BLACK,
                    binding.periodicityField,
                    binding.daysFieldEditing,
                    binding.periodicityFieldSecondPart
                )
                binding.periodicityDaysNotEntered.visibility = View.INVISIBLE
            }
        })
    }

    private fun changeColor(color: Int, vararg views: View) {
        views.forEach {
            if (it is TextView)
                it.setTextColor(color)
            if (it is EditText) {
                val colorStateList = ColorStateList.valueOf(color)
                ViewCompat.setBackgroundTintList(it, colorStateList)
            }
        }
    }
}