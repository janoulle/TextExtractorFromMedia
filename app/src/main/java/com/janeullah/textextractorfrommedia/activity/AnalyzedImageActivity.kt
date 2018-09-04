package com.janeullah.textextractorfrommedia.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Looper
import android.support.v4.app.NavUtils
import android.support.v4.app.TaskStackBuilder
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.janeullah.textextractorfrommedia.R
import com.janeullah.textextractorfrommedia.constants.IntentNames
import com.janeullah.textextractorfrommedia.data.ImageInformation
import com.janeullah.textextractorfrommedia.service.recognizeText
import kotlinx.android.synthetic.main.activity_analyzed_image.*
import kotlinx.android.synthetic.main.content_analyzed_image.*


class AnalyzedImageActivity : AppCompatActivity() {
    private lateinit var tweetMediaItem: ImageInformation
    private lateinit var listOfMediaEntities: ArrayList<ImageInformation>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analyzed_image)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //https://developer.android.com/training/data-storage/files#OpenFileInternal
        tweetMediaItem = intent.getParcelableExtra(IntentNames.TWEET_MEDIA_ITEM)

        listOfMediaEntities = intent.getParcelableArrayListExtra(IntentNames.TWEET_MEDIA_LIST)

        val options = BitmapFactory.Options().apply {
            inPreferredConfig = Bitmap.Config.RGB_565
        }
        val imageBitmap = BitmapFactory.decodeFile(tweetMediaItem.cachedAbsolutePath, options)

        imageBitmap?.let {

            val mediaImage: FirebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap)
            val onDeviceTextRecognizer = FirebaseVision.getInstance().onDeviceTextRecognizer
            onDeviceTextRecognizer.processImage(mediaImage)
                    .addOnSuccessListener { success ->
                        Log.i("imgRecognitionSuccess", "In UI Thread ${Looper.myLooper() == Looper.getMainLooper()}")
                        val result = recognizeText(success, tweetMediaItem)
                        Log.i("imgRecognitionSuccess", result.toString())

                        analyzedMediaImage.text = result.text
                    }
                    .addOnFailureListener { failure ->
                        Log.i("imgRecognitionFailure", "In UI Thread ${Looper.myLooper() == Looper.getMainLooper()}")
                        Log.e("imgRecognitionFailure", "Failed to process $failure", failure)
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
            android.R.id.home -> {

                // Using navigateUpFromSameTask() is suitable only when your app is the owner of the current task
                // (that is, the user began this task from your app). If that's not true and your activity was started in a task
                // that belongs to a different app, then navigating Up should create a new task that belongs to your app,
                // which requires that you create a new back stack.

                // Respond to the action bar's Up/Home button
                val upIntent: Intent? = NavUtils.getParentActivityIntent(this).apply {
                    this?.putExtra(IntentNames.TWEET_ID, tweetMediaItem.tweetId)
                    this?.putExtra(IntentNames.TWEET_MEDIA_ITEM, tweetMediaItem)
                    this?.putParcelableArrayListExtra(IntentNames.TWEET_MEDIA_LIST, listOfMediaEntities)
                }

                when {
                    upIntent == null -> throw IllegalStateException("No Parent Activity Intent")
                    NavUtils.shouldUpRecreateTask(this, upIntent) -> {
                        // This activity is NOT part of this app's task, so create a new task
                        // when navigating up, with a synthesized back stack.
                        TaskStackBuilder.create(this)
                                // Add all of this activity's parents to the back stack
                                .addNextIntentWithParentStack(upIntent)
                                // Navigate up to the closest parent
                                .startActivities()
                    }
                    else -> {
                        // This activity is part of this app's task, so simply
                        // navigate up to the logical parent activity.
                        NavUtils.navigateUpTo(this, upIntent)
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
