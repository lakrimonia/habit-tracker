package com.example.habittracker

import io.github.kakaocup.kakao.screen.Screen
import io.github.kakaocup.kakao.text.KTextView

class HabitCreatingOrEditingScreen : Screen<HabitCreatingOrEditingScreen>() {
    val pageTitle = KTextView { withId(R.id.page_title) }
    val nameField = KTextView { withId(R.id.name_field) }
}