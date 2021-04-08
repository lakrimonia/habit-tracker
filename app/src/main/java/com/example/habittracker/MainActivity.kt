package com.example.habittracker

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.habittracker.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), MainActivityCallback,
    NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val noUIFragment = NoUIFragment()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(noUIFragment, NoUIFragment.TAG).commit()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, MainPageFragment.newInstance(noUIFragment.habitsList)).commit()
        }
        binding.navView.setNavigationItemSelectedListener(this)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun addHabit() {
        val noUIFragment =
            supportFragmentManager.findFragmentByTag(NoUIFragment.TAG) as NoUIFragment
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragment_container,
                HabitCreatingOrEditingFragment.newInstance(noUIFragment.habitId)
            )
            .commit()
    }

    override fun editHabit(habit: Habit) {
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragment_container,
                HabitCreatingOrEditingFragment.newInstance(habit, habit.id)
            )
            .commit()
    }

    override fun addFinalHabit(habit: Habit) {
        val noUIFragment =
            supportFragmentManager.findFragmentByTag(NoUIFragment.TAG) as NoUIFragment
        if (habit.id != noUIFragment.habitId) {
            var deletedHabitIndex = 0
            for (i in 0 until noUIFragment.habitsList.size) {
                if (noUIFragment.habitsList[i].id == habit.id) {
                    deletedHabitIndex = i
                    break
                }
            }
            noUIFragment.habitsList.removeAt(deletedHabitIndex)
        } else noUIFragment.habitId++
        when (habit.priority) {
            HabitPriority.HIGH -> noUIFragment.habitsList.add(0, habit)
            HabitPriority.MEDIUM -> noUIFragment.habitsList.add(noUIFragment.habitsList.size / 2, habit)
            HabitPriority.LOW -> noUIFragment.habitsList.add(habit)
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, MainPageFragment.newInstance(noUIFragment.habitsList))
            .commit()
    }

    override fun returnToMainPage() {
        val noUIFragment =
            supportFragmentManager.findFragmentByTag(NoUIFragment.TAG) as NoUIFragment
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, MainPageFragment.newInstance(noUIFragment.habitsList))
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.main_page) {
            val habitsList =
                (supportFragmentManager.findFragmentByTag(NoUIFragment.TAG) as NoUIFragment).habitsList
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, MainPageFragment.newInstance(habitsList))
                .commit()
        }
        if (item.itemId == R.id.about_app)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, AboutAppFragment())
                .commit()
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}