package com.janeullah.textextractorfrommedia.service

import android.app.Activity
import android.graphics.Bitmap
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.janeullah.textextractorfrommedia.constants.RecognizableTypes
import com.twitter.sdk.android.core.models.Tweet

interface Tweetable {

    fun processTweet(tweet: Tweet?, activity: Activity, recognizer: RecognizableTypes)
    //fun processTweet(tweet: Tweet?, context: Context, recognizer: RecognizableTypes)

    fun generateImageRequest(imageUrl: String, maxWidth: Int, maxHeight: Int, activity: Activity, recognizableTypes: RecognizableTypes): ImageRequest {

        Log.d("tweetable", "In UI Thread ${Looper.myLooper() == Looper.getMainLooper()}")
        val imageRequest = ImageRequest(imageUrl,
                Response.Listener { response ->
                    Log.i("imageDownloadSuccess", "Response: $response, type $recognizableTypes, imageUrl $imageUrl")

                    val recognizer = if (recognizableTypes == RecognizableTypes.IMAGE) TextRecognizerFromImageImpl() else TextRecognizerFromDocumentImpl()

                    recognizer.recognizeText(response)
                },
                maxWidth,
                maxHeight,
                ImageView.ScaleType.FIT_CENTER,
                Bitmap.Config.RGB_565,
                Response.ErrorListener { error ->
                    Log.e("imageDownloadFailure", "type $recognizableTypes, imageUrl $imageUrl", error)
                    Toast.makeText(activity, "Error fetching image from url $imageUrl", Toast.LENGTH_LONG).show()
                }
        )
        return imageRequest
    }
}