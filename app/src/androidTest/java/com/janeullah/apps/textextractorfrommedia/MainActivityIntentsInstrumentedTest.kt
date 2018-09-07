package com.janeullah.apps.textextractorfrommedia

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.Intents.intending
import android.support.test.espresso.intent.matcher.BundleMatchers.hasEntry
import android.support.test.espresso.intent.matcher.IntentMatchers.*
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import com.janeullah.textextractorfrommedia.R
import com.janeullah.textextractorfrommedia.activity.MainActivity
import com.janeullah.textextractorfrommedia.constants.IntentNames
import org.hamcrest.CoreMatchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * https://github.com/googlesamples/android-testing/blob/master/ui/espresso/IntentsBasicSample/app/src/androidTest/java/com/example/android/testing/espresso/BasicSample/DialerActivityTest.java
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityIntentsInstrumentedTest {
    private val PACKAGE_TEXT_EXTRACTOR_FROM_MEDIA = "com.janeullah.textextractorfrommedia"

    @get:Rule
    var mMainActivityTestRule = IntentsTestRule(MainActivity::class.java)

    @Before
    fun stubAllExternalIntents() {
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        intending(not(isInternal())).respondWith(ActivityResult(Activity.RESULT_OK, null))
    }


    @Test
    fun enterTweetId_WithoutMediaAttached_RemainOnSameActivity() {
        val tweetId = "24323424"
        val message = "This tweet id ($tweetId) does not have any embedded media objects"
        onView(withId(R.id.tweetIdField))
                .perform(typeText(tweetId), closeSoftKeyboard())

        onView(withId(R.id.submitTweetId)).perform(click())

        // Verify that an intent to the DisplayTweetImages activity was sent with the correct action, phone
        // number and package. Think of Intents intended API as the equivalent to Mockito's verify.
        intended(allOf(
                hasAction(equalTo(Intent.ACTION_VIEW)),
                hasCategories(hasItem(equalTo(Intent.CATEGORY_BROWSABLE))),
                hasExtras(allOf(
                        hasEntry(equalTo(IntentNames.TWEET_ID), equalTo(tweetId)))),
                toPackage(PACKAGE_TEXT_EXTRACTOR_FROM_MEDIA)))
    }
}
