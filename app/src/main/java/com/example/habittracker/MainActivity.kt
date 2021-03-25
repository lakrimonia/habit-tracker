package com.example.habittracker

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.habittracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var habitsList: MutableList<Habit>
    private val createNewHabitCode = 0
    private val editHabitCode = 1
    private var habitId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        habitsList = mutableListOf()
        binding.recyclerView.adapter =
            HabitsAdapter(habitsList) { habit -> habitItemOnClick(habit) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_CANCELED) return
        data?.let {
            val id = it.getIntExtra("id", habitId)
            val name = it.getStringExtra("name").toString()
            val description = it.getStringExtra("description").toString()
            val priority: HabitPriority = it.getSerializableExtra("priority") as HabitPriority
            val type = it.getSerializableExtra("type") as HabitType
            val periodicity = it.getSerializableExtra("periodicity") as Pair<Int, Int>
            val color = it.getIntExtra("color", 0)
            if (requestCode == editHabitCode) {
                var deletedHabitIndex = 0
                for (i in 0 until habitsList.size) {
                    if (habitsList[i].id == id) {
                        deletedHabitIndex = i
                        break
                    }
                }
                habitsList.removeAt(deletedHabitIndex)
            }
            if (requestCode == createNewHabitCode) {
                habitId++
            }
            val habit = Habit(habitId, name, description, priority, type, periodicity, color)
            when (habit.priority) {
                HabitPriority.HIGH -> habitsList.add(0, habit)
                HabitPriority.MEDIUM -> habitsList.add(habitsList.size / 2, habit)
                HabitPriority.LOW -> habitsList.add(habit)
            }

            binding.recyclerView.adapter?.notifyDataSetChanged()
        }

    }

    private fun habitItemOnClick(habit: Habit) {
        val intent = Intent(this, HabitCreatingOrEditingActivity::class.java)
        intent.putExtra("id", habit.id)
        intent.putExtra("request code", editHabitCode)
        intent.putExtra("name", habit.name)
        intent.putExtra("description", habit.description)
        intent.putExtra("priority", habit.priority)
        intent.putExtra("type", habit.type)
        intent.putExtra("periodicity", habit.periodicityTimesPerDay)
        intent.putExtra("color", habit.color)
        startActivityForResult(intent, editHabitCode)
    }

    fun fabOnClick(view: View) {
        startActivityForResult(
            Intent(this, HabitCreatingOrEditingActivity::class.java),
            createNewHabitCode
        )
    }
}