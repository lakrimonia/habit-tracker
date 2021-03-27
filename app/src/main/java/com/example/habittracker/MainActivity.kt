package com.example.habittracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
        data?.getParcelableExtra<Habit>("habit")?.let {
            if (requestCode == editHabitCode) {
                var deletedHabitIndex = 0
                for (i in 0 until habitsList.size) {
                    if (habitsList[i].id == it.id) {
                        deletedHabitIndex = i
                        break
                    }
                }
                habitsList.removeAt(deletedHabitIndex)
            }
            if (requestCode == createNewHabitCode) {
                habitId++
            }
            when (it.priority) {
                HabitPriority.HIGH -> habitsList.add(0, it)
                HabitPriority.MEDIUM -> habitsList.add(habitsList.size / 2, it)
                HabitPriority.LOW -> habitsList.add(it)
            }
            binding.recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    private fun habitItemOnClick(habit: Habit) {
        val intent = Intent(this, HabitCreatingOrEditingActivity::class.java)
        intent.putExtra("habit", habit)
        intent.putExtra("id", habit.id)
        intent.putExtra("request code", editHabitCode)
        startActivityForResult(intent, editHabitCode)
    }

    fun fabOnClick(view: View) {
        val intent = Intent(this, HabitCreatingOrEditingActivity::class.java)
        intent.putExtra("id", habitId)
        startActivityForResult(
            intent,
            createNewHabitCode
        )
    }
}