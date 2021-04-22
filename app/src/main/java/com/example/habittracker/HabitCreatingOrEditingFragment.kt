package com.example.habittracker

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
import androidx.fragment.app.activityViewModels
import com.example.habittracker.databinding.FragmentHabitCreatingOrEditingBinding

class HabitCreatingOrEditingFragment : Fragment() {
    companion object {
        private const val HABIT_TO_EDIT = "habit to edit"

        @JvmStatic
        fun newInstance(habitToEdit: Habit) = HabitCreatingOrEditingFragment().apply {
            arguments = Bundle().apply {
                putParcelable(HABIT_TO_EDIT, habitToEdit)
            }
        }
    }

    private var _binding: FragmentHabitCreatingOrEditingBinding? = null
    private val binding get() = _binding!!
    private var callback: MainActivityCallback? = null
    private val viewModel: HabitCreatingOrEditingViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = activity as MainActivityCallback
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitCreatingOrEditingBinding.inflate(inflater, container, false)
        arguments?.getParcelable<Habit>(HABIT_TO_EDIT).let {
            if (it != null)
                viewModel.editHabit(it)
            else viewModel.createHabit()
        }
        createColorTable()
        binding.floatingActionButton.setOnClickListener {
            viewModel.clickOnFab()
        }
        viewModel.setValues()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pageTitle.text = resources.getString(R.string.habit_creating_title)
        binding.nameFieldEditing.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                viewModel.setName(s.toString())
            }
        })
        binding.descriptionFieldEditing.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                viewModel.setDescription(s.toString())
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

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val times = s.toString()
                if (times != "")
                    viewModel.setPeriodicityTimes(times.toInt())
                else viewModel.setPeriodicityTimes(null)
            }
        })

        binding.daysFieldEditing.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val days = s.toString()
                if (days != "")
                    viewModel.setPeriodicityDays(days.toInt())
                else viewModel.setPeriodicityDays(null)
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

    private fun createColorTable() {
        val colors = intArrayOf(
            Color.parseColor("#ff0000"),
            Color.parseColor("#ffff00"),
            Color.parseColor("#00ff00"),
            Color.parseColor("#00ffff"),
            Color.parseColor("#0000ff"),
            Color.parseColor("#ff00ff"),
            Color.parseColor("#ff0000")
        )
        val gradient = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors)
        val colorButtonWidth = 50
        val width = 2 * colorButtonWidth * 16
        val height = 100
        val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        gradient.setBounds(0, 0, width, height)
        gradient.draw(c)
        binding.colorsScroll.background = b.toDrawable(resources)

        for (i in 0 until 16) {
            val button = ImageButton(context)
            val buttonParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            val displayMetrics = resources.displayMetrics
            buttonParams.height =
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    colorButtonWidth.toFloat(),
                    displayMetrics
                )
                    .toInt()
            buttonParams.width =
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    colorButtonWidth.toFloat(),
                    displayMetrics
                )
                    .toInt()
            buttonParams.rightMargin = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                colorButtonWidth.toFloat(),
                displayMetrics
            )
                .toInt()
            buttonParams.topMargin = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                20f,
                displayMetrics
            )
                .toInt()
            button.layoutParams = buttonParams
            val x = 2 * colorButtonWidth * i + colorButtonWidth / 2
            val y = 0
            val color = ColorDrawable(b.getPixel(x, y))
            button.background = color
            button.setOnClickListener { _ ->
                binding.currentColorIcon.setImageDrawable(color)
                viewModel.setColor(b.getPixel(x, y))
            }
            ViewCompat.setElevation(button, 5f)
            binding.colorsScroll.addView(button)
        }
    }
}