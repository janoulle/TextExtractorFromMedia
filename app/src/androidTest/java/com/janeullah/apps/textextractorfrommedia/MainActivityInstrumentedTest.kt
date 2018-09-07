package com.janeullah.apps.textextractorfrommedia


import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.RootMatchers.withDecorView
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import com.google.common.base.Preconditions.checkArgument
import com.janeullah.textextractorfrommedia.R
import com.janeullah.textextractorfrommedia.activity.MainActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.hamcrest.TypeSafeMatcher
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 * https://github.com/googlesamples/android-testing
 * https://github.com/googlesamples/android-testing/tree/master/ui/espresso/CustomMatcherSample
 * https://fernandocejas.com/2017/02/03/android-testing-with-kotlin/
 * https://android.jlelse.eu/robolectric-unit-testing-framework-for-android-b78ebac0b411
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityInstrumentedTest {

    /**
     * A custom [Matcher] which matches an item in a [RecyclerView] by its text.
     *
     *
     *
     * View constraints:
     *
     *  * View must be a child of a [RecyclerView]
     *
     *
     * @param itemText the text to match
     * @return Matcher that matches text in the given view
     */
    private fun withItemText(itemText: String): Matcher<View> {
        checkArgument(!TextUtils.isEmpty(itemText), "itemText cannot be null or empty")
        return object : TypeSafeMatcher<View>() {
            public override fun matchesSafely(item: View): Boolean {
                return allOf<View>(
                        isDescendantOfA(isAssignableFrom(RecyclerView::class.java)),
                        withText(itemText)).matches(item)
            }

            override fun describeTo(description: Description) {
                description.appendText("is isDescendantOfA RV with text $itemText")
            }
        }
    }

    //https://stackoverflow.com/questions/29945087/kotlin-and-new-activitytestrule-the-rule-must-be-public
    //https://kotlinlang.org/docs/reference/annotations.html#java-annotations
    @get:Rule var mMainActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.janeullah.textextractorfrommedia", appContext.packageName)
    }

    @Test
    fun enterTweetId_NonNumericValue() {
        val errorMessage = "Please enter a valid tweet id!"
        //enter tweet id
        onView(withId(R.id.tweetIdField)).perform(typeText("abcde"), closeSoftKeyboard())

        //click submit tweet button
        onView(withId(R.id.submitTweetId)).perform(click())

        //verify toast with error message is displayed
        onView(withText(errorMessage)).inRoot(withDecorView(not(mMainActivityTestRule.activity.window.decorView))).check(matches(isDisplayed()))
    }


}

