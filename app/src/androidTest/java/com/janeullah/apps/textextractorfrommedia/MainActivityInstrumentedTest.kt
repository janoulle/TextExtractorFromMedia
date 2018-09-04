package com.janeullah.apps.textextractorfrommedia


import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
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
import org.hamcrest.TypeSafeMatcher
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
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
    @Rule @JvmField
    var mNotesActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.janeullah.textextractorfrommedia", appContext.packageName)
    }

    @Ignore("TODO: mock the api calls")
    @Test
    fun enterTweetId() {
        //enter tweet id
        onView(withId(R.id.tweetIdField)).perform(typeText("1027598458746593280"), closeSoftKeyboard())

        //click submit tweet button
        onView(withId(R.id.submitTweetId)).perform(click())

        //verify next activity is launched
    }

}
