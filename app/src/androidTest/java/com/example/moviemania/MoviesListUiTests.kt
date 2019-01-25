package com.example.moviemania


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MoviesListUiTests {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun movies_list_loading_text_is_shown() {
        onView(withId(R.id.txtProgress)).check(matches(isDisplayed()))
    }

    @Test
    fun movies_list_loading_text_with_string_is_shown() {
        onView(withId(R.id.txtProgress)).check(matches(isDisplayed()))
        onView(withText("Loading movies...."))
            .check(matches(isDisplayed()))
    }

}
