package com.janeullah.textextractorfrommedia.activity

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.janeullah.textextractorfrommedia.BuildConfig
import com.janeullah.textextractorfrommedia.R
import com.janeullah.textextractorfrommedia.constants.IntentNames
import com.janeullah.textextractorfrommedia.service.TweetProcessor
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.models.Tweet
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {


    private fun extractTweetIdFromUrl(intent: Intent): String {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {sharedTweetText ->
            Log.i("tweetShared", sharedTweetText)
            //https://gist.github.com/gruber/8891611
            //https://daringfireball.net/2010/07/improved_regex_for_matching_urls
            val regex = """
(?i)\b((?:https?:(?:/{1,3}|[a-z0-9%])|[a-z0-9.\-]+[.](?:com|net|org|edu|gov|mil|aero|asia|biz|cat|coop|info|int|jobs|mobi|museum|name|post|pro|tel|travel|xxx|ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|ax|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|bs|bt|bv|bw|by|bz|ca|cc|cd|cf|cg|ch|ci|ck|cl|cm|cn|co|cr|cs|cu|cv|cx|cy|cz|dd|de|dj|dk|dm|do|dz|ec|ee|eg|eh|er|es|et|eu|fi|fj|fk|fm|fo|fr|ga|gb|gd|ge|gf|gg|gh|gi|gl|gm|gn|gp|gq|gr|gs|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id|ie|il|im|in|io|iq|ir|is|it|je|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|me|mg|mh|mk|ml|mm|mn|mo|mp|mq|mr|ms|mt|mu|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nu|nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|ps|pt|pw|py|qa|re|ro|rs|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|Ja|sk|sl|sm|sn|so|sr|ss|st|su|sv|sx|sy|sz|tc|td|tf|tg|th|tj|tk|tl|tm|tn|to|tp|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|uz|va|vc|ve|vg|vi|vn|vu|wf|ws|ye|yt|yu|za|zm|zw)/)(?:[^\s()<>{}\[\]]+|\([^\s()]*?\([^\s()]+\)[^\s()]*?\)|\([^\s]+?\))+(?:\([^\s()]*?\([^\s()]+\)[^\s()]*?\)|\([^\s]+?\)|[^\s`!()\[\]{};:'".,<>?«»“”‘’])|(?:(?<!@)[a-z0-9]+(?:[.\-][a-z0-9]+)*[.](?:com|net|org|edu|gov|mil|aero|asia|biz|cat|coop|info|int|jobs|mobi|museum|name|post|pro|tel|travel|xxx|ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|ax|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|bs|bt|bv|bw|by|bz|ca|cc|cd|cf|cg|ch|ci|ck|cl|cm|cn|co|cr|cs|cu|cv|cx|cy|cz|dd|de|dj|dk|dm|do|dz|ec|ee|eg|eh|er|es|et|eu|fi|fj|fk|fm|fo|fr|ga|gb|gd|ge|gf|gg|gh|gi|gl|gm|gn|gp|gq|gr|gs|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id|ie|il|im|in|io|iq|ir|is|it|je|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|me|mg|mh|mk|ml|mm|mn|mo|mp|mq|mr|ms|mt|mu|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nu|nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|ps|pt|pw|py|qa|re|ro|rs|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|Ja|sk|sl|sm|sn|so|sr|ss|st|su|sv|sx|sy|sz|tc|td|tf|tg|th|tj|tk|tl|tm|tn|to|tp|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|uz|va|vc|ve|vg|vi|vn|vu|wf|ws|ye|yt|yu|za|zm|zw)\b/?(?!@)))
            """.trimIndent().toRegex()
            regex.find(sharedTweetText)?.let {extractedUrl ->
                //match digits in the url (not very robust....)
                //shared tweet url pattern - https://twitter.com/user/status/10000000000000000001?s=09
                val digitFinder = "\\d+".toRegex()
                val extractedTweetId = digitFinder.find(extractedUrl.value)?.groups?.get(0)?.value.toString()
                Log.i("extractedTweetId", extractedTweetId)
                return extractedTweetId
            }
        }
        return ""
    }

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
            if (!isValidTweetId(tweetId)) {
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
                    val tweetIdVar = extractTweetIdFromUrl(intent)
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

    private fun isValidTweetId(tweetId: String?): Boolean {
        try {
            tweetId?.toLong()
        } catch (e: NumberFormatException) {
            return false
        }
        return !TextUtils.isEmpty(tweetId)
    }


}
