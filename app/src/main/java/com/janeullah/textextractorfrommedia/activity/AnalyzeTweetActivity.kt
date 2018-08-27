package com.janeullah.textextractorfrommedia.activity

import android.os.Bundle
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.janeullah.textextractorfrommedia.BuildConfig
import com.janeullah.textextractorfrommedia.R
import com.janeullah.textextractorfrommedia.constants.Constants.IntentNames.PARSE_MODE
import com.janeullah.textextractorfrommedia.constants.Constants.IntentNames.TWEET_ID
import com.janeullah.textextractorfrommedia.constants.RecognizableTypes
import com.janeullah.textextractorfrommedia.constants.getMatchingRecognizer
import com.janeullah.textextractorfrommedia.service.TweetImpl
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.models.Tweet


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
        val twitterApiClient = TwitterCore.getInstance().apiClient
        val statusesService = twitterApiClient.statusesService

        Thread(Runnable {
            Log.d("mainActivity", "In UI Thread ${Looper.myLooper() == Looper.getMainLooper()}")
            val call = statusesService.show(tweetId, null, null, null)
            call.enqueue(object : Callback<Tweet>() {
                override fun success(result: Result<Tweet>?) {
                    Log.i("tweetCallSuccess", result?.toString())
                    TweetImpl().processTweet(result?.data, this@AnalyzeTweetActivity, parseMode)
                }

                override fun failure(exception: TwitterException?) {
                    Log.e("tweetCallFailure", "Failed to fetch tweet by $tweetId", exception)
                    Toast.makeText(this@AnalyzeTweetActivity, "Error fetching tweet id $tweetId", Toast.LENGTH_LONG).show()
                }

            })
        }).start()

    }

    //https://kotlinlang.org/docs/reference/basic-types.html
    private fun parseTweetId(stringExtra: String?): Long {
        if (stringExtra != null) {
            return stringExtra.toLong()
        }
        return 0L
    }

}
