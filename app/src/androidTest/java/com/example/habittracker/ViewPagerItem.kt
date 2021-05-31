package com.example.habittracker

import android.view.View
import io.github.kakaocup.kakao.pager2.KViewPagerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import org.hamcrest.Matcher

class ViewPagerItem(parent: Matcher<View>) : KViewPagerItem<ViewPagerItem>(parent) {
    val recycler = KRecyclerView({
        withId(R.id.recycler_view)
    }, {
        itemType(::HabitItem)
    })
}