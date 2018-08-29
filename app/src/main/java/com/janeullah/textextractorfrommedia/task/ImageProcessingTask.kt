package com.janeullah.textextractorfrommedia.task

import android.app.Activity
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Looper
import android.util.Log
import android.widget.TextView
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.janeullah.textextractorfrommedia.R
import com.janeullah.textextractorfrommedia.constants.RecognizableTypes
import com.janeullah.textextractorfrommedia.service.TextRecognizerFromDocumentImpl
import com.janeullah.textextractorfrommedia.service.TextRecognizerFromImageImpl
import java.lang.ref.WeakReference

//processing the bitmap via firebase ml
class ImageProcessingTask(val context: WeakReference<Activity>, val recognizableTypes: RecognizableTypes) : AsyncTask<Bitmap, Void, Void>() {
    override fun doInBackground(vararg imageBitmaps: Bitmap?): Void? {
        if (!imageBitmaps.isEmpty()) {
            val image = imageBitmaps[0]
            Log.i("imageProcessingAsync", "In UI Thread ${Looper.myLooper() == Looper.getMainLooper()}")
            //todo: update UI thread with info
            val nullableActivity = context.get()
            image?.let {
                val mediaImage: FirebaseVisionImage = FirebaseVisionImage.fromBitmap(image)
                when (recognizableTypes) {
                    RecognizableTypes.DOCUMENT -> {
                        val cloudTextRecognizer = FirebaseVision.getInstance()
                                .cloudDocumentTextRecognizer
                        cloudTextRecognizer.processImage(mediaImage)
                                .addOnSuccessListener {success ->
                                    Log.i("docRecognitionSuccess", "In UI Thread ${Looper.myLooper() == Looper.getMainLooper()}")
                                    val result = TextRecognizerFromDocumentImpl().recognizeText(success)
                                    Log.i("docRecognitionSuccess", result.toString())
                                    nullableActivity?.let { activity ->
                                        //todo: only works for one image at a time! Fix me
                                        val textView: TextView = activity.findViewById(R.id.analyzedTweet)
                                        textView.text = result.text
                                    }
                                }
                                .addOnFailureListener {failure ->
                                    Log.i("docRecognitionFailure", "In UI Thread ${Looper.myLooper() == Looper.getMainLooper()}")
                                    Log.e("docRecognitionFailure", "Failed to process $failure", failure)
                                }

                    }
                    RecognizableTypes.IMAGE -> {
                        val onDeviceTextRecognizer = FirebaseVision.getInstance().onDeviceTextRecognizer
                        onDeviceTextRecognizer.processImage(mediaImage)
                                .addOnSuccessListener {success ->
                                    Log.i("imgRecognitionSuccess", "In UI Thread ${Looper.myLooper() == Looper.getMainLooper()}")
                                    val result = TextRecognizerFromImageImpl().recognizeText(success)
                                    Log.i("imgRecognitionSuccess", result.toString())
                                    nullableActivity?.let { activity ->
                                        //todo: only works for one image at a time! Fix me
                                        val textView: TextView = activity.findViewById(R.id.analyzedTweet)
                                        textView.text = result.text
                                    }
                                }
                                .addOnFailureListener {failure ->
                                    Log.i("imgRecognitionFailure", "In UI Thread ${Looper.myLooper() == Looper.getMainLooper()}")
                                    Log.e("imgRecognitionFailure", "Failed to process $failure", failure)
                                }
                    }
                }
            }

        }
        return null
    }

}