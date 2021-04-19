package com.example.habittracker

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.graphics.drawable.toDrawable
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createColorTable()
        binding.pageTitle.text = resources.getString(R.string.habit_creating_title)

        viewModel.habitToEdit.observe(viewLifecycleOwner, {
            setValuesToFields(it)
        })

        binding.floatingActionButton.setOnClickListener {
            saveChanges()
        }

        viewModel.saveChanges.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {
                callback?.returnToMainPage()
            }
        })
    }

    private fun setValuesToFields(habit: Habit?) {
        binding.pageTitle.text = resources.getString(R.string.habit_editing_title)
        binding.nameFieldEditing.setText(habit?.name ?: "")
        binding.descriptionFieldEditing.setText(habit?.description ?: "")
        binding.priorityFieldEditing.setSelection(
            when (habit?.priority) {
                HabitPriority.HIGH -> 0
                HabitPriority.MEDIUM -> 1
                HabitPriority.LOW -> 2
                null -> 0
            }
        )
        binding.typeFieldEditing.check(
            when (habit?.type) {
                HabitType.GOOD -> binding.goodHabitButton.id
                HabitType.BAD -> binding.badHabitButton.id
                null -> binding.goodHabitButton.id
            }
        )
        binding.timesFieldEditing.setText((habit?.periodicityTimesPerDay?.first ?: "").toString())
        binding.daysFieldEditing.setText((habit?.periodicityTimesPerDay?.second ?: "").toString())
        binding.currentColorIcon.setImageDrawable(ColorDrawable(habit?.color ?: Color.RED))
    }

    private fun saveChanges() {
        val name = binding.nameFieldEditing.text.toString()
        val description = binding.descriptionFieldEditing.text.toString()
        val priority = HabitPriority.parse(binding.priorityFieldEditing.selectedItem.toString())
        val type = when (binding.goodHabitButton.isChecked) {
            true -> HabitType.GOOD
            false -> HabitType.BAD
        }
        val periodicityTimes = binding.timesFieldEditing.text.toString().toInt()
        val periodicityDays = binding.daysFieldEditing.text.toString().toInt()
        val color = (binding.currentColorIcon.drawable as ColorDrawable).color
        viewModel.clickOnFab(
            name,
            description,
            priority,
            type,
            periodicityTimes to periodicityDays,
            color
        )
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
            val button = Button(context)
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
            button.setOnClickListener { _ -> binding.currentColorIcon.setImageDrawable(color) }
            binding.colorsScroll.addView(button)
        }
    }
}