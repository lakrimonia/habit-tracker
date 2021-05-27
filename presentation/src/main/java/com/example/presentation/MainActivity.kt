package com.example.presentation

import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView

interface MainActivityCallback {
    fun addHabit()
    fun editHabit()
    fun returnToMainPage()
}

class MainActivity: AppCompatActivity(), MainActivityCallback,
    NavigationView.OnNavigationItemSelectedListener {
    override fun addHabit() {
        TODO("Not yet implemented")
    }

    override fun editHabit() {
        TODO("Not yet implemented")
    }

    override fun returnToMainPage() {
        TODO("Not yet implemented")
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("Not yet implemented")
    }
}