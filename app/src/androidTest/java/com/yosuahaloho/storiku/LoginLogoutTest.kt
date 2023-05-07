package com.yosuahaloho.storiku

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.yosuahaloho.storiku.presentation.home.HomeActivity
import com.yosuahaloho.storiku.presentation.started.StartedActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Yosua on 06/05/2023
 */
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class LoginLogoutTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    private val email = "haloho12@gmail.com"
    private val password = "yosua123"

    @Test
    fun loginTest() {
        // Run Started Activity
        ActivityScenario.launch(StartedActivity::class.java)

        // Check if button to login page is displayed, and then click it if displayed
        onView(withId(R.id.btn_to_login_page)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_to_login_page)).perform(click())

        // Check if edit text email is displayed, and then fill it with email
        onView(withId(R.id.ed_login_email)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_login_email)).perform(typeText(email), closeSoftKeyboard())

        // Check if edit text password is displayed, and then fill it with password
        onView(withId(R.id.ed_login_password)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_login_password)).perform(typeText(password), closeSoftKeyboard())

        // Check if button login is displayed, and then perform click on it
        onView(withId(R.id.btn_login)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_login)).perform(click())
    }

    @Test
    fun logoutTest() {
        // Run Home Activity
        ActivityScenario.launch(HomeActivity::class.java)

        // Open action bar logout
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)

        // Check if button logout is displayed, and then perform click on it
        onView(withText("Log out")).check(matches(isDisplayed()))
        onView(withText("Log out")).perform(click())
    }

}