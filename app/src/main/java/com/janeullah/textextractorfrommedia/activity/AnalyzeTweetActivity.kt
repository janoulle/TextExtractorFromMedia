package com.janeullah.textextractorfrommedia.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod
import android.util.Log
import com.janeullah.textextractorfrommedia.BuildConfig
import com.janeullah.textextractorfrommedia.R
import com.janeullah.textextractorfrommedia.constants.IntentNames.PARSE_MODE
import com.janeullah.textextractorfrommedia.constants.IntentNames.TWEET_ID
import com.janeullah.textextractorfrommedia.constants.RecognizableTypes
import com.janeullah.textextractorfrommedia.constants.getMatchingRecognizer
import com.janeullah.textextractorfrommedia.task.TweetAsyncTask
import com.twitter.sdk.android.core.DefaultLogger
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig
import kotlinx.android.synthetic.main.activity_analyze_tweet.*
import java.lang.ref.WeakReference


//https://developer.android.com/training/basics/firstapp/starting-activity
//https://github.com/twitter/twitter-kit-android/wiki/Show-Tweets
class AnalyzeTweetActivity : AppCompatActivity() {
    val tag: String = "AnalyzeTweetActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analyze_tweet)
        val config = TwitterConfig.Builder(this)
                .logger(DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(TwitterAuthConfig(BuildConfig.TWITTER_CONSUMER_KEY, BuildConfig.TWITTER_CONSUMER_SECRET))
                .debug(true)
                .build()
        Twitter.initialize(config)

        // Get the Intent that started this activity and extract the string
        val tweetId = parseTweetId(intent.getStringExtra(TWEET_ID))
        val parseMode = getMatchingRecognizer(intent.getStringExtra(PARSE_MODE))
                ?: RecognizableTypes.DOCUMENT
        analyzedTweet.movementMethod = ScrollingMovementMethod()

        TweetAsyncTask(WeakReference(this), parseMode).execute(tweetId)

    }

    //https://kotlinlang.org/docs/reference/basic-types.html
    private fun parseTweetId(stringExtra: String?): Long {
        if (stringExtra != null) {
            return stringExtra.toLong()
        }
        return 0L
    }

}
