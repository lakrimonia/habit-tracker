package com.example.habittracker

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.commit
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
        if (savedInstanceState == null)
            supportFragmentManager.commit {
                add(R.id.fragment_container, MainPageFragment())
            }
        binding.navView.setNavigationItemSelectedListener(this)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun addHabit() {
        supportFragmentManager.commit {
            replace(R.id.fragment_container, HabitCreatingOrEditingFragment())
            addToBackStack(null)
        }
    }

    override fun editHabit(habit: Habit) {
        supportFragmentManager.commit {
            replace(R.id.fragment_container, HabitCreatingOrEditingFragment.newInstance(habit))
            addToBackStack(null)
        }
    }

    override fun returnToMainPage() {
        supportFragmentManager.popBackStack()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.main_page)
            supportFragmentManager.commit {
                replace(R.id.fragment_container, MainPageFragment())
            }
        if (item.itemId == R.id.about_app)
            supportFragmentManager.commit {
                replace(R.id.fragment_container, AboutAppFragment())
            }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}