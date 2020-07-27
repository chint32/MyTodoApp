package com.example.mytodoapp

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.Espresso.onView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.mytodoapp.view.CategoryFragment
import com.example.mytodoapp.view.TodoActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class CategoryActivityTest {

    @Test
    fun isActivityOnDisplay() {
        val activityScenario = ActivityScenario.launch(TodoActivity::class.java)
        onView(withId(R.id.main_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.cardView3)).check(matches(isDisplayed()))
    }

}