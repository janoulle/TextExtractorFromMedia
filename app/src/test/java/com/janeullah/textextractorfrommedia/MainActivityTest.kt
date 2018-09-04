package com.janeullah.textextractorfrommedia

import android.widget.Button
import android.widget.EditText
import com.janeullah.textextractorfrommedia.activity.MainActivity
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowToast


@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    @Test
    fun enterTweetId_shouldNotStartDisplayTweetsActivity() {
        val errorMessage = "Please enter a valid tweet id!"

        val activity = Robolectric.setupActivity(MainActivity::class.java)

        activity.findViewById<EditText>(R.id.tweetIdField).setText("abcde")
        activity.findViewById<Button>(R.id.submitTweetId).performClick()

        assertEquals(errorMessage, ShadowToast.getTextOfLatestToast())
    }
}