package com.yosuahaloho.storiku

import android.content.Intent
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.yosuahaloho.storiku.presentation.home.HomeActivity
import com.yosuahaloho.storiku.presentation.list_story.ListStoryFragment
import com.yosuahaloho.storiku.presentation.started.StartedActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

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
//        activityRule.scenario.onActivity {
//            it.supportFragmentManager.beginTransaction()
//                .replace(R.id.main_fragment_container_view, ListStoryFragment())
//                .commit()
//        }
//        launchFragmentInHiltContainer<ListStoryFragment>()

    }

//    @Inject
//    lateinit var fragmentFactory

    // Open Home Activity
    @get:Rule
    var activityRule: ActivityScenarioRule<HomeActivity> = ActivityScenarioRule(HomeActivity::class.java)


    @Test
    fun checkIfLogoutSuccess() {
        // Open Action Bar Menu
        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)

        // Check view with text "Log out" and perform click on it
        Espresso.onView(ViewMatchers.withText("Log out")).perform(ViewActions.click())

        // Check if log out success with check if started activity is displayed
        Espresso.onView(ViewMatchers.withId(R.id.started_activity)).check(matches(ViewMatchers.isDisplayed()))
    }

}