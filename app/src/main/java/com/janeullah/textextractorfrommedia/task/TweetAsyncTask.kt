package com.janeullah.textextractorfrommedia.task

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.janeullah.textextractorfrommedia.activity.MainActivity
import com.janeullah.textextractorfrommedia.constants.IntentNames
import com.janeullah.textextractorfrommedia.constants.RecognizableTypes
import com.janeullah.textextractorfrommedia.service.TweetProcessor
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Tweet
import java.lang.ref.WeakReference

//Fetching the tweet using twitter api
//todo: is there a better way to architect this so that the asynctask does not directly call activity ?
//https://medium.com/google-developer-experts/weakreference-in-android-dd1e66b9be9d
//https://kotlinlang.org/docs/reference/classes.html
//https://stackoverflow.com/questions/3243215/how-to-use-weakreference-in-java-and-android-development
//https://stackoverflow.com/questions/46797916/this-field-leaks-a-context-object
//https://github.com/facebook/facebook-android-sdk/blob/master/facebook-login/src/main/java/com/facebook/login/widget/ToolTipPopup.java
//https://community.oracle.com/blogs/enicholas/2006/05/04/understanding-weak-references
//https://stackoverflow.com/questions/3243215/how-to-use-weakreference-in-java-and-android-development
class TweetAsyncTask(val context: WeakReference<Activity>, val parseMode: RecognizableTypes) : AsyncTask<Long, Void, Void>() {

    fun displayErrorAndGoBack(tweetId: String, message: String, activity: Activity) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
        val intent = Intent(activity, MainActivity::class.java).apply {
            putExtra(IntentNames.TWEET_ID, tweetId)
        }
        activity.startActivity(intent)
    }

    override fun doInBackground(vararg tweetIds: Long?): Void? {
        if (tweetIds.isNotEmpty()) {
            val firstTweetId = tweetIds[0]
            val twitterApiClient = TwitterCore.getInstance().apiClient
            val statusesService = twitterApiClient.statusesService

            Log.i("tweetAsyncTask", "In UI Thread ${Looper.myLooper() == Looper.getMainLooper()}")
            val call = statusesService.show(firstTweetId, null, null, null)

            val activity = context.get()
            call.enqueue(object : Callback<Tweet>() {
                override fun success(result: Result<Tweet>?) {
                    Log.i("tweetCallSuccess", "In UI Thread ${Looper.myLooper() == Looper.getMainLooper()} ")
                    val mediaEntities = result?.data?.extendedEntities?.media ?: listOf()

                    activity?.let {
                        if (mediaEntities.isEmpty()) {
                            displayErrorAndGoBack(firstTweetId.toString(), "ErroThis tweet id ($firstTweetId) does not have any embedded media objects", it)
                        } else {
                            TweetProcessor().processTweetMedia(mediaEntities, it, parseMode)
                        }
                    }
                }

                override fun failure(exception: TwitterException?) {
                    Log.e("tweetCallFailure", "Failed to fetch tweet by $firstTweetId", exception)
                    activity?.let {
                        displayErrorAndGoBack(firstTweetId.toString(),"Error fetching tweet id $firstTweetId", it)
                    }
                }
            })
        }

        return null
    }

}

