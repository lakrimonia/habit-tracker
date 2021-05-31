package com.example.habittracker

import android.app.Application
import com.example.habittracker.di.ApplicationComponent

abstract class ApplicationWithDaggerComponent : Application() {
    lateinit var applicationComponent: ApplicationComponent
        protected set
}