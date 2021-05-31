package com.example.habittracker

import android.view.View
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

class HabitItem(parent: Matcher<View>) : KRecyclerItem<HabitItem>(parent) {
    val name = KTextView(parent) { withId(R.id.habit_name) }
    val description = KTextView(parent) { withId(R.id.habit_description) }
    val periodicity = KTextView(parent) { withId(R.id.habit_periodicity) }
    val date = KTextView(parent) { withId(R.id.habit_changing_date) }
    val completionsCount = KTextView(parent) { withId(R.id.completions_count) }
    val markAsCompletedButton = KButton(parent) { withId(R.id.habit_completed_button) }
}