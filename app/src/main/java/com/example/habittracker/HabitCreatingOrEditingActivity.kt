package com.example.habittracker

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.graphics.drawable.toDrawable
import com.example.habittracker.databinding.ActivityHabitCreatingOrEditingBinding

class HabitCreatingOrEditingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHabitCreatingOrEditingBinding
    private val createNewHabitCode = 0
    private val editHabitCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHabitCreatingOrEditingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        createColorTable()
        val requestCode = intent.getIntExtra("request code", 0)
        if (requestCode == editHabitCode)
            setValuesToFields()
        else binding.pageTitle.text = "Создание привычки"
    }

    private fun setValuesToFields() {
        binding.pageTitle.text = "Редактирование привычки"
        binding.nameFieldEditing.setText(intent.getStringExtra("name"))
        binding.descriptionFieldEditing.setText(intent.getStringExtra("description"))
        binding.priorityFieldEditing.setSelection(
            when (intent.getSerializableExtra("priority") as HabitPriority) {
                HabitPriority.HIGH -> 0
                HabitPriority.MEDIUM -> 1
                HabitPriority.LOW -> 2
            }
        )
        binding.typeFieldEditing.check(
            when (intent.getSerializableExtra("type") as HabitType) {
                HabitType.GOOD -> binding.goodHabitButton.id
                HabitType.BAD -> binding.badHabitButton.id
            }
        )
        val periodicity = intent.getSerializableExtra("periodicity") as Pair<Int, Int>
        binding.timesFieldEditing.setText(periodicity.first.toString())
        binding.daysFieldEditing.setText(periodicity.second.toString())
        binding.currentColorIcon.setImageDrawable(ColorDrawable(intent.getIntExtra("color", 0)))
    }

    fun saveChanges(view: View) {
        if (binding.nameFieldEditing.text.isEmpty() || binding.descriptionField.text.isEmpty() || binding.timesFieldEditing.text.isEmpty() || binding.daysFieldEditing.text.isEmpty()) {
            setResult(Activity.RESULT_CANCELED)
        } else {
            val priorityToString: Map<String, HabitPriority> = mapOf(
                "Высокий" to HabitPriority.HIGH,
                "Средний" to HabitPriority.MEDIUM,
                "Низкий" to HabitPriority.LOW
            )
            val name = binding.nameFieldEditing.text.toString()
            val description = binding.descriptionFieldEditing.text.toString()
            val priority = priorityToString[binding.priorityFieldEditing.selectedItem.toString()]
            val type = when (binding.goodHabitButton.isChecked) {
                true -> HabitType.GOOD
                false -> HabitType.BAD
            }
            val periodicityTimes = binding.timesFieldEditing.text.toString().toInt()
            val periodicityDays = binding.daysFieldEditing.text.toString().toInt()
            val color = (binding.currentColorIcon.drawable as ColorDrawable).color
            Intent().let {
                it.putExtra("id", intent.getIntExtra("id", 0))
                it.putExtra("name", name)
                it.putExtra("description", description)
                it.putExtra("priority", priority)
                it.putExtra("type", type)
                it.putExtra("periodicity", periodicityTimes to periodicityDays)
                it.putExtra("color", color)
                setResult(Activity.RESULT_OK, it)
            }
        }
        finish()
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
            val button = Button(this)
            val buttonParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            val displayMetrics = resources.displayMetrics
            buttonParams.height =
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, colorButtonWidth.toFloat(), displayMetrics)
                    .toInt()
            buttonParams.width =
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, colorButtonWidth.toFloat(), displayMetrics)
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

            val x = 2 *colorButtonWidth * i + colorButtonWidth / 2
            val y = 0
            val color = ColorDrawable(b.getPixel(x, y))
            button.background = color
            button.setOnClickListener { _ ->  binding.currentColorIcon.setImageDrawable(color) }
            binding.colorsScroll.addView(button)
        }
    }
}