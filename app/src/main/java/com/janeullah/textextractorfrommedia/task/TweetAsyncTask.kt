package com.janeullah.textextractorfrommedia.task

import android.app.Activity
import android.os.AsyncTask
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.janeullah.textextractorfrommedia.R
import com.janeullah.textextractorfrommedia.constants.RecognizableTypes
import com.janeullah.textextractorfrommedia.service.TweetImpl
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Tweet
import java.lang.ref.WeakReference

class TweetAsyncTask(val context: WeakReference<Activity>, val parseMode: RecognizableTypes) : AsyncTask<Long, Void, Long>() {

    override fun doInBackground(vararg tweetIds: Long?): Long {
        if (tweetIds.isNotEmpty()) {
            val firstTweetId = tweetIds[0]
            val twitterApiClient = TwitterCore.getInstance().apiClient
            val statusesService = twitterApiClient.statusesService

            Log.d("tweetAsyncTask", "In UI Thread ${Looper.myLooper() == Looper.getMainLooper()}")
            val call = statusesService.show(firstTweetId, null, null, null)

            val activity = context.get()
            call.enqueue(object : Callback<Tweet>() {
                override fun success(result: Result<Tweet>?) {
                    Log.i("tweetCallSuccess", result?.toString())
                    if (isActivityOkay(activity)) {
                        activity?.let { TweetImpl().processTweet(result?.data, it, parseMode) }
                    }
                }

                override fun failure(exception: TwitterException?) {
                    Log.e("tweetCallFailure", "Failed to fetch tweet by ${R.string.tweetId}", exception)
                    if (isActivityOkay(activity)) {
                        activity?.let { Toast.makeText(it, "Error fetching tweet id ${R.string.tweetId}", Toast.LENGTH_LONG).show() }
                    }
                }

            })
        }
        return -1L
    }

    fun isActivityOkay(activity: Activity?) : Boolean {
        return activity == null || activity.isFinishing || activity.isDestroyed
    }

}