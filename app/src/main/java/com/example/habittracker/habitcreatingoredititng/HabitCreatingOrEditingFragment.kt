package com.example.habittracker.habitcreatingoredititng

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.example.domain.HabitPriority
import com.example.domain.HabitType
import com.example.habittracker.ApplicationWithDaggerComponent
import com.example.habittracker.ColorPicker
import com.example.habittracker.MainActivityCallback
import com.example.habittracker.databinding.FragmentHabitCreatingOrEditingBinding
import javax.inject.Inject

class HabitCreatingOrEditingFragment : Fragment(), OnBackPressedListener {
    private var _binding: FragmentHabitCreatingOrEditingBinding? = null
    private val binding get() = _binding!!
    private var callback: MainActivityCallback? = null

    @Inject
    lateinit var viewModel: HabitCreatingOrEditingViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = activity as MainActivityCallback
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity().application as ApplicationWithDaggerComponent)
            .applicationComponent
            .getViewModelSubcomponent()
            .create()
            .inject(this)
        _binding = FragmentHabitCreatingOrEditingBinding.inflate(inflater, container, false)
        ColorPicker.create(requireContext(), resources, binding.colorsScroll) {
            val color = it?.background as ColorDrawable
            binding.currentColorIcon.setImageDrawable(color)
            viewModel.setColor(color.color.toString())
        }
        binding.saveChangesButton.setOnClickListener {
            viewModel.clickOnFab()
        }

        viewModel.setValues()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.mediatorLiveData.observe(viewLifecycleOwner, { })
        viewModel.pageTitle.observe(viewLifecycleOwner, {
            binding.pageTitle.text = it
        })
        binding.nameFieldEditing.addTextChangedListener(object : FieldTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setName(s.toString())
            }
        })
        binding.descriptionFieldEditing.addTextChangedListener(object : FieldTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setDescription(s.toString())
            }
        })
        binding.priorityFieldEditing.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

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
        binding.timesFieldEditing.addTextChangedListener(object : FieldTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setPeriodicityTimes(s.toString())
            }
        })
        binding.daysFieldEditing.addTextChangedListener(object : FieldTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setPeriodicityDays(s.toString())
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
            changeColor(
                isNotEntered,
                binding.nameNotEntered,
                "*нужно заполнить",
                binding.nameField,
                binding.nameFieldEditing
            )
        })
        viewModel.timesNotEntered.observe(viewLifecycleOwner, { isNotEntered ->
            changeColor(
                isNotEntered,
                binding.periodicityTimesError,
                "*нужно заполнить",
                binding.periodicityField,
                binding.timesFieldEditing,
                binding.periodicityFieldFirstPart
            )
        })
        viewModel.daysNotEntered.observe(viewLifecycleOwner, { isNotEntered ->
            changeColor(
                isNotEntered,
                binding.periodicityDaysError,
                "*нужно заполнить",
                binding.periodicityField,
                binding.daysFieldEditing,
                binding.periodicityFieldSecondPart
            )
        })
        viewModel.timesEqualsZero.observe(viewLifecycleOwner, { equalsZero ->
            changeColor(
                equalsZero,
                binding.periodicityTimesError,
                "*значение должно быть больше 0",
                binding.periodicityField,
                binding.timesFieldEditing,
                binding.periodicityFieldFirstPart
            )
        })
        viewModel.daysEqualsZero.observe(viewLifecycleOwner, { equalsZero ->
            changeColor(
                equalsZero,
                binding.periodicityDaysError,
                "*значение должно быть больше 0",
                binding.periodicityField,
                binding.daysFieldEditing,
                binding.periodicityFieldSecondPart
            )
        })
    }

    private fun changeColor(
        shouldShowErrorMessage: Boolean,
        errorMessageView: TextView,
        errorMessage: String,
        vararg viewsToChangeColor: View
    ) {
        if (shouldShowErrorMessage) {
            changeColor(Color.RED, viewsToChangeColor)
            errorMessageView.text = errorMessage
            errorMessageView.visibility = View.VISIBLE
        } else {
            changeColor(Color.BLACK, viewsToChangeColor)
            errorMessageView.visibility = View.INVISIBLE
        }
    }

    private fun changeColor(color: Int, views: Array<out View>) {
        views.forEach {
            if (it is TextView)
                it.setTextColor(color)
            if (it is EditText) {
                val colorStateList = ColorStateList.valueOf(color)
                ViewCompat.setBackgroundTintList(it, colorStateList)
            }
        }
    }

    override fun onBackPressed() {
        viewModel.clearFields()
    }
}