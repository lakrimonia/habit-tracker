package com.example.habittracker

import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.pager2.KViewPager2
import io.github.kakaocup.kakao.screen.Screen
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView

class MainPageScreen : Screen<MainPageScreen>() {
    val viewPager = KViewPager2({
        withId(R.id.view_pager)
    }, {
        itemType(::ViewPagerItem)
    })

    val bottomSheet = KTextView { withId(R.id.bottom_sheet_title) }
    val sortByPriorityAscendingButton = KButton { withId(R.id.sort_by_priority_from_low_to_high) }
    val sortByPriorityDescendingButton = KButton { withId(R.id.sort_by_priority_from_high_to_low) }
    val sortByNameAscendingButton = KButton { withId(R.id.sort_by_name_from_low_to_high) }
    val sortByNameDescendingButton = KButton { withId(R.id.sort_by_name_from_high_to_low) }
    val sortByChangingDateAscendingButton = KButton { withId(R.id.sort_by_date_from_low_to_high) }
    val sortByChangingDateDescendingButton = KButton { withId(R.id.sort_by_date_from_high_to_low) }
    val findByName = KEditText { withId(R.id.find_by_name) }

    val resetFiltersButton = KButton { withId(R.id.reset_all_filters) }
}