package com.janeullah.textextractorfrommedia.activity

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.janeullah.textextractorfrommedia.BuildConfig
import com.janeullah.textextractorfrommedia.R
import com.janeullah.textextractorfrommedia.constants.IntentNames
import com.janeullah.textextractorfrommedia.service.TweetProcessor
import com.janeullah.textextractorfrommedia.util.StringHelper
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.models.Tweet
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {
    private val stringHelper = StringHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val config = TwitterConfig.Builder(this)
                .logger(DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(TwitterAuthConfig(BuildConfig.TWITTER_CONSUMER_KEY, BuildConfig.TWITTER_CONSUMER_SECRET))
                .debug(true)
                .build()
        Twitter.initialize(config)


        //listener for clicks on the submit button
        submitTweetId.setOnClickListener {
            submitTweetId.isEnabled = false
            val tweetId = tweetIdField.text.toString()
            if (!stringHelper.isValidLong(tweetId)) {
                Toast.makeText(this@MainActivity, "Please enter a valid tweet id!", Toast.LENGTH_LONG).show()
                submitTweetId.isEnabled = true
            } else {

                val twitterApiClient = TwitterCore.getInstance().apiClient
                val statusesService = twitterApiClient.statusesService

                val call = statusesService.show(tweetId.toLong(), null, null, null)

                //val progressBarView = LayoutInflater.from(this).inflate(R.layout.progress_bar, null, false)
                //indeterminateBar.visibility = ProgressBar.VISIBLE

                call.enqueue(object : Callback<Tweet>() {
                    override fun success(result: Result<Tweet>?) {
                        Log.i("tweetCallSuccess", "In UI Thread ${Looper.myLooper() == Looper.getMainLooper()} ")
                        val mediaEntities = result?.data?.extendedEntities?.media ?: listOf()

                        if (mediaEntities.isEmpty()) {
                            submitTweetId.isEnabled = true
                            Toast.makeText(this@MainActivity, "This tweet id ($tweetId) does not have any embedded media objects", Toast.LENGTH_LONG).show()
                        } else {
                            val intent = Intent(this@MainActivity, DisplayTweetImages::class.java).apply {
                                putExtra(IntentNames.TWEET_ID, tweetIdField.text.toString())
                                putParcelableArrayListExtra(IntentNames.TWEET_MEDIA_LIST, ArrayList(TweetProcessor(tweetIdField.text.toString()).getMediaList(mediaEntities)))
                            }
                            startActivity(intent)
                        }
                    }

                    override fun failure(exception: TwitterException?) {
                        submitTweetId.isEnabled = true
                        Log.e("tweetCallFailure", "Failed to fetch tweet by $tweetId", exception)
                        Toast.makeText(this@MainActivity, "Error fetching tweet id $tweetId with message: ${exception?.message}", Toast.LENGTH_LONG).show()
                        //indeterminateBar.visibility = ProgressBar.INVISIBLE

                    }
                })
            }
        }

        //check the intent launching this activity
        when {
            //when data is sent to this activity from another app
            intent.action == Intent.ACTION_SEND -> {
                if ("text/plain" == intent.type) {
                    val tweetShared = intent.getStringExtra(Intent.EXTRA_TEXT)
                    Log.i("mainActivity", "tweetShared $tweetShared")
                    val tweetIdVar = stringHelper.extractTweetIdFromUrl(intent.getStringExtra(Intent.EXTRA_TEXT))
                    Log.i("mainActivity", "tweetId extracted $tweetIdVar")
                    tweetIdField.setText(tweetIdVar)
                    submitTweetId.performClick()
                }
            }
            //app launched via 'normal' means
            else -> {
                val tweetIdVar = intent.getStringExtra(IntentNames.TWEET_ID) ?: ""
                tweetIdField.setText(tweetIdVar)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        submitTweetId.isEnabled = true
    }

}
