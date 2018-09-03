package com.janeullah.textextractorfrommedia.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Looper
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analyzed_image)
        setSupportActionBar(toolbar)

        //https://developer.android.com/training/data-storage/files#OpenFileInternal
        val tweetMediaItem = intent.getParcelableExtra<ImageInformation>(IntentNames.TWEET_MEDIA_ITEM)

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
            else -> super.onOptionsItemSelected(item)
        }
    }
}
