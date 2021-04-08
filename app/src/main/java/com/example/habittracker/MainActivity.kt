package com.example.habittracker

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.habittracker.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

class MainActivity : AppCompatActivity(), MainActivityCallback,
    NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val hl = HabitsListFragment()
        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction().add(hl, HabitsListFragment.TAG).commit()
        binding.viewPager.adapter = HabitsListFragmentAdapter(this, hl.habitsList)
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        setSupportActionBar(binding.toolbar)
        binding.navView.setNavigationItemSelectedListener(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            if (position == 0)
                tab.text = "Полезные"
            else tab.text = "Вредные"
        }.attach()
        binding.floatingActionButton.setOnClickListener {
            addHabit()
        }
    }


    private fun addHabit() {
        binding.tabLayout.visibility = View.GONE
        binding.floatingActionButton.visibility = View.GONE
        val hl =
            supportFragmentManager.findFragmentByTag(HabitsListFragment.TAG) as HabitsListFragment
        binding.viewPager.adapter = HabitCreatingOrEditingFragmentAdapter(this, hl.habitId)
    }

    override fun editHabit(habit: Habit) {
        binding.tabLayout.visibility = View.GONE
        binding.floatingActionButton.visibility = View.GONE
        binding.viewPager.adapter = HabitCreatingOrEditingFragmentAdapter(this, habit.id, habit)
    }

    override fun addFinalHabit(habit: Habit) {
        val hl =
            supportFragmentManager.findFragmentByTag(HabitsListFragment.TAG) as HabitsListFragment
        if (habit.id != hl.habitId) {
            var deletedHabitIndex = 0
            for (i in 0 until hl.habitsList.size) {
                if (hl.habitsList[i].id == habit.id) {
                    deletedHabitIndex = i
                    break
                }
            }
            hl.habitsList.removeAt(deletedHabitIndex)
        } else hl.habitId++
        when (habit.priority) {
            HabitPriority.HIGH -> hl.habitsList.add(0, habit)
            HabitPriority.MEDIUM -> hl.habitsList.add(hl.habitsList.size / 2, habit)
            HabitPriority.LOW -> hl.habitsList.add(habit)
        }

        binding.viewPager.adapter = HabitsListFragmentAdapter(this, hl.habitsList)
        binding.tabLayout.visibility = View.VISIBLE
        binding.floatingActionButton.visibility = View.VISIBLE
    }

    override fun returnToMainPage() {
        val hl =
            supportFragmentManager.findFragmentByTag(HabitsListFragment.TAG) as HabitsListFragment
        binding.viewPager.adapter = HabitsListFragmentAdapter(this, hl.habitsList)
        binding.tabLayout.visibility = View.VISIBLE
        binding.floatingActionButton.visibility = View.VISIBLE
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.main_page) {
            binding.tabLayout.visibility = View.VISIBLE
            binding.viewPager.adapter = HabitsListFragmentAdapter(
                this,
                (supportFragmentManager.findFragmentByTag("habits list") as HabitsListFragment).habitsList
            )
        }
        if (item.itemId == R.id.about_app) {
            binding.tabLayout.visibility = View.GONE
            binding.viewPager.adapter = AboutAppFragmentAdapter(this)
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    class HabitsListFragmentAdapter(
        activity: AppCompatActivity,
        val habits: ArrayList<Habit>
    ) : FragmentStateAdapter(activity) {
        override fun getItemCount() = 2

        override fun createFragment(position: Int): Fragment {
            if (position == 0) {
                val goodHabits =
                    ArrayList(habits.filter { habit -> habit.type == HabitType.GOOD })
                return MainPageFragment.newInstance(goodHabits)
            }
            val badHabits = ArrayList(habits.filter { habit -> habit.type == HabitType.BAD })
            return MainPageFragment.newInstance(badHabits)
        }
    }

    class HabitCreatingOrEditingFragmentAdapter(
        activity: AppCompatActivity,
        val habitId: Int,
        val habit: Habit? = null
    ) : FragmentStateAdapter(activity) {
        override fun getItemCount() = 1

        override fun createFragment(position: Int): Fragment {
            if (habit == null)
                return HabitCreatingOrEditingFragment.newInstance(habitId)
            return HabitCreatingOrEditingFragment.newInstance(habit, habitId)
        }
    }

    class AboutAppFragmentAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount() = 1

        override fun createFragment(position: Int): Fragment = AboutAppFragment()
    }
}