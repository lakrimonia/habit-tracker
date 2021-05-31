package com.example.habittracker

import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test

class MainPageTests {
    @Rule
    @JvmField
    var rule = ActivityScenarioRule(MainActivity::class.java)

    private val mainPageScreen = MainPageScreen()

    @Test
    fun viewPagerHasTwoPages() {
        mainPageScreen {
            viewPager {
                hasSize(2)
            }
        }
    }

    @Test
    fun goodHabitsPageHasRightCount() {
        mainPageScreen {
            viewPager {
                childAt<ViewPagerItem>(0) {
                    recycler {
                        hasSize(3)
                    }
                }
            }
        }
    }

    @Test
    fun badHabitsPageHasRightCount() {
        mainPageScreen {
            viewPager {
                childAt<ViewPagerItem>(1) {
                    recycler {
                        hasSize(3)
                    }
                }
            }
        }
    }

    @Test
    fun uiChangesWhenGoodHabitMarkedAsCompleted() {
        mainPageScreen {
            viewPager {
                childAt<ViewPagerItem>(0) {
                    recycler {
                        firstChild<HabitItem> {
                            markAsCompletedButton { click() }
                            completionsCount { hasText("2/3") }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun uiChangesWhenBadHabitMarkedAsCompleted() {
        mainPageScreen {
            viewPager {
                childAt<ViewPagerItem>(1) {
                    recycler {
                        firstChild<HabitItem> {
                            markAsCompletedButton { click() }
                            completionsCount { hasText("2/1") }
                        }
                    }
                }
            }
        }
    }
}