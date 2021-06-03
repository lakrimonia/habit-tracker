package com.example.habittracker

import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.After
import org.junit.Rule
import org.junit.Test

class SortingAndFilterTests {
    @Rule
    @JvmField
    var rule = ActivityScenarioRule(MainActivity::class.java)

    private val mainPageScreen = MainPageScreen()

    @After
    fun tearDown() {
        mainPageScreen { resetFiltersButton { click() } }
    }

    @Test
    fun sortingByPriorityAscendingWorksRightForGoodHabits() {
        mainPageScreen {
            bottomSheet { click() }
            sortByPriorityAscendingButton { click() }
            viewPager {
                childAt<ViewPagerItem>(0) {
                    recycler {
                        childAt<HabitItem>(0) { name { hasText("Гулять") } }
                        childAt<HabitItem>(1) { name { hasText("Вода") } }
                        childAt<HabitItem>(2) { name { hasText("Спорт") } }
                    }
                }
            }
        }
    }

    @Test
    fun sortingByPriorityDescendingWorksRightForGoodHabits() {
        mainPageScreen {
            bottomSheet { click() }
            sortByPriorityDescendingButton { click() }
            viewPager {
                childAt<ViewPagerItem>(0) {
                    recycler {
                        childAt<HabitItem>(0) { name { hasText("Спорт") } }
                        childAt<HabitItem>(1) { name { hasText("Вода") } }
                        childAt<HabitItem>(2) { name { hasText("Гулять") } }
                    }
                }
            }
        }
    }

    @Test
    fun sortingByPriorityAscendingWorksRightForBadHabits() {
        mainPageScreen {
            bottomSheet { click() }
            sortByPriorityAscendingButton { click() }
            viewPager {
                childAt<ViewPagerItem>(1) {
                    recycler {
                        childAt<HabitItem>(0) { name { hasText("Фастфуд") } }
                        childAt<HabitItem>(1) { name { hasText("Алкоголь") } }
                        childAt<HabitItem>(2) { name { hasText("Прогуливать пары") } }
                    }
                }
            }
        }
    }

    @Test
    fun sortingByPriorityDescendingWorksRightForBadHabits() {
        mainPageScreen {
            bottomSheet { click() }
            sortByPriorityDescendingButton { click() }
            viewPager {
                childAt<ViewPagerItem>(1) {
                    recycler {
                        childAt<HabitItem>(0) { name { hasText("Прогуливать пары") } }
                        childAt<HabitItem>(1) { name { hasText("Алкоголь") } }
                        childAt<HabitItem>(2) { name { hasText("Фастфуд") } }
                    }
                }
            }
        }
    }

    @Test
    fun sortingByNameAscendingWorksRightForGoodHabits() {
        mainPageScreen {
            bottomSheet { click() }
            sortByNameAscendingButton { click() }
            viewPager {
                childAt<ViewPagerItem>(0) {
                    recycler {
                        childAt<HabitItem>(0) { name { hasText("Вода") } }
                        childAt<HabitItem>(1) { name { hasText("Гулять") } }
                        childAt<HabitItem>(2) { name { hasText("Спорт") } }
                    }
                }
            }
        }
    }

    @Test
    fun sortingByNameDescendingWorksRightForGoodHabits() {
        mainPageScreen {
            bottomSheet { click() }
            sortByNameDescendingButton { click() }
            viewPager {
                childAt<ViewPagerItem>(0) {
                    recycler {
                        childAt<HabitItem>(0) { name { hasText("Спорт") } }
                        childAt<HabitItem>(1) { name { hasText("Гулять") } }
                        childAt<HabitItem>(2) { name { hasText("Вода") } }
                    }
                }
            }
        }
    }

    @Test
    fun sortingByNameAscendingWorksRightForBadHabits() {
        mainPageScreen {
            bottomSheet { click() }
            sortByNameAscendingButton { click() }
            viewPager {
                childAt<ViewPagerItem>(1) {
                    recycler {
                        childAt<HabitItem>(0) { name { hasText("Алкоголь") } }
                        childAt<HabitItem>(1) { name { hasText("Прогуливать пары") } }
                        childAt<HabitItem>(2) { name { hasText("Фастфуд") } }
                    }
                }
            }
        }
    }

    @Test
    fun sortingByNameDescendingWorksRightForBadHabits() {
        mainPageScreen {
            bottomSheet { click() }
            sortByNameDescendingButton { click() }
            viewPager {
                childAt<ViewPagerItem>(1) {
                    recycler {
                        childAt<HabitItem>(0) { name { hasText("Фастфуд") } }
                        childAt<HabitItem>(1) { name { hasText("Прогуливать пары") } }
                        childAt<HabitItem>(2) { name { hasText("Алкоголь") } }
                    }
                }
            }
        }
    }

    @Test
    fun sortingByChangingDateAscendingWorksRightForGoodHabits() {
        mainPageScreen {
            bottomSheet { click() }
            sortByChangingDateAscendingButton { click() }
            viewPager {
                childAt<ViewPagerItem>(0) {
                    recycler {
                        childAt<HabitItem>(0) { name { hasText("Вода") } }
                        childAt<HabitItem>(1) { name { hasText("Гулять") } }
                        childAt<HabitItem>(2) { name { hasText("Спорт") } }
                    }
                }
            }
        }
    }

    @Test
    fun sortingByChangingDateDescendingWorksRightForGoodHabits() {
        mainPageScreen {
            bottomSheet { click() }
            sortByChangingDateDescendingButton { click() }
            viewPager {
                childAt<ViewPagerItem>(0) {
                    recycler {
                        childAt<HabitItem>(0) { name { hasText("Спорт") } }
                        childAt<HabitItem>(1) { name { hasText("Гулять") } }
                        childAt<HabitItem>(2) { name { hasText("Вода") } }
                    }
                }
            }
        }
    }

    @Test
    fun sortingByChangingDateAscendingWorksRightForBadHabits() {
        mainPageScreen {
            bottomSheet { click() }
            sortByChangingDateAscendingButton { click() }
            viewPager {
                childAt<ViewPagerItem>(1) {
                    recycler {
                        childAt<HabitItem>(0) { name { hasText("Прогуливать пары") } }
                        childAt<HabitItem>(1) { name { hasText("Алкоголь") } }
                        childAt<HabitItem>(2) { name { hasText("Фастфуд") } }
                    }
                }
            }
        }
    }

    @Test
    fun sortingByChangingDateDescendingWorksRightForBadHabits() {
        mainPageScreen {
            bottomSheet { click() }
            sortByChangingDateDescendingButton { click() }
            viewPager {
                childAt<ViewPagerItem>(1) {
                    recycler {
                        childAt<HabitItem>(0) { name { hasText("Фастфуд") } }
                        childAt<HabitItem>(1) { name { hasText("Алкоголь") } }
                        childAt<HabitItem>(2) { name { hasText("Прогуливать пары") } }
                    }
                }
            }
        }
    }

    @Test
    fun simpleFindingByNameForGoodHabits() {
        mainPageScreen {
            bottomSheet { click() }
            findByName { this.replaceText("В") }
            viewPager {
                childAt<ViewPagerItem>(0) {
                    recycler {
                        hasSize(1)
                        firstChild<HabitItem> { name.hasText("Вода") }
                    }
                }
            }
        }
    }

    @Test
    fun findingByNameHasZeroResultForGoodHabits() {
        mainPageScreen {
            bottomSheet { click() }
            findByName { this.replaceText("Ы") }
            viewPager {
                childAt<ViewPagerItem>(0) {
                    recycler {
                        hasSize(0)
                    }
                }
            }
        }
    }

    @Test
    fun typeSomethingInFindByNameAndEraseForGoodHabits() {
        mainPageScreen {
            bottomSheet { click() }
            findByName { this.replaceText("В") }
            findByName { this.replaceText("") }
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
    fun simpleFindingByNameForBadHabits() {
        mainPageScreen {
            bottomSheet { click() }
            findByName { this.replaceText("Ф") }
            viewPager {
                childAt<ViewPagerItem>(1) {
                    recycler {
                        hasSize(1)
                        firstChild<HabitItem> { name.hasText("Фастфуд") }
                    }
                }
            }
        }
    }

    @Test
    fun findingByNameHasZeroResultForBadHabits() {
        mainPageScreen {
            bottomSheet { click() }
            findByName { this.replaceText("Ы") }
            viewPager {
                childAt<ViewPagerItem>(1) {
                    recycler {
                        hasSize(0)
                    }
                }
            }
        }
    }

    @Test
    fun typeSomethingInFindByNameAndEraseForBadHabits() {
        mainPageScreen {
            bottomSheet { click() }
            findByName { this.replaceText("В") }
            findByName { this.replaceText("") }
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
    fun justResetFiltersForGoodHabits() {
        mainPageScreen {
            bottomSheet { click() }
            resetFiltersButton { click() }
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
    fun sortAndFilterAndThenResetForGoodHabits() {
        mainPageScreen {
            bottomSheet { click() }
            sortByNameAscendingButton { click() }
            findByName { replaceText("j") }
            resetFiltersButton { click() }
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
    fun justResetFiltersForBadHabits() {
        mainPageScreen {
            bottomSheet { click() }
            resetFiltersButton { click() }
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
    fun sortAndFilterAndThenResetForBadHabits() {
        mainPageScreen {
            bottomSheet { click() }
            sortByNameAscendingButton { click() }
            findByName { replaceText("j") }
            resetFiltersButton { click() }
            viewPager {
                childAt<ViewPagerItem>(1) {
                    recycler {
                        hasSize(3)
                    }
                }
            }
        }
    }
}