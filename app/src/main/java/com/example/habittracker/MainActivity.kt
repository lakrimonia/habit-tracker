package com.example.habittracker

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.commit
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.example.habittracker.databinding.ActivityMainBinding
import com.example.habittracker.databinding.NavHeaderMainBinding
import com.example.habittracker.habitcreatingoredititng.HabitCreatingOrEditingFragment
import com.example.habittracker.mainpage.MainPageFragment
import com.example.habittracker.model.HabitRepository
import com.google.android.material.navigation.NavigationView
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class MainActivity : AppCompatActivity(), MainActivityCallback,
    NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        if (savedInstanceState == null)
            supportFragmentManager.commit {
                add(R.id.fragment_container, MainPageFragment())
            }
        setSupportActionBar(binding.toolbar)
        drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerToggle.isDrawerIndicatorEnabled = true
        binding.drawerLayout.addDrawerListener(drawerToggle)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.navView.setNavigationItemSelectedListener(this)
        setAvatar()
    }

    private fun setAvatar(){
        val imageView = binding.navView.getHeaderView(0).findViewById<ImageView>(R.id.avatar)
        val placeholder = CircularProgressDrawable(this)
        placeholder.centerRadius=30f
        placeholder.strokeWidth=5f
        placeholder.start()

        Glide.with(this)
            .load("https://mem-baza.ru/_ph/1/2/990419753.jpg?1600930052")
            .override(300,300)
            .placeholder(placeholder)
            .error(R.drawable.ic_avatar_loading_error)
            .circleCrop()
            .into(imageView)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (drawerToggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        if (supportFragmentManager.backStackEntryCount > 0) {
            returnToMainPage()
            HabitRepository.setHabitToEdit(null)
        } else
            super.onBackPressed()
    }

    override fun addHabit() {
        supportFragmentManager.commit {
            replace(R.id.fragment_container, HabitCreatingOrEditingFragment())
            addToBackStack(null)
        }
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        drawerToggle.isDrawerIndicatorEnabled = false
    }

    override fun editHabit() {
        supportFragmentManager.commit {
            replace(R.id.fragment_container, HabitCreatingOrEditingFragment())
            addToBackStack(null)
        }
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        drawerToggle.isDrawerIndicatorEnabled = false
    }

    override fun returnToMainPage() {
        supportFragmentManager.popBackStack()
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        drawerToggle.isDrawerIndicatorEnabled = true
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