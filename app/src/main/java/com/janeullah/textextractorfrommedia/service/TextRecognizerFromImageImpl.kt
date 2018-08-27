package com.janeullah.textextractorfrommedia.service

import android.graphics.Bitmap
import android.os.Looper
import android.util.Log
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.janeullah.textextractorfrommedia.data.TweetMediaText

//todo: implement listener to update activity that cares about this response
class TextRecognizerFromImageImpl : TextRecognizable {

    override fun recognizeText(image: Bitmap) {
        val mediaImage = FirebaseVisionImage.fromBitmap(image)
        val textRecognizer = FirebaseVision.getInstance().onDeviceTextRecognizer
        textRecognizer.processImage(mediaImage)
                .addOnSuccessListener {
                    Log.d("imgRecognitionSuccess", "In UI Thread ${Looper.myLooper() == Looper.getMainLooper()}")
                    val result = recognizeText(it)
                    Log.i("imgRecognitionSuccess", result.toString())
                }
                .addOnFailureListener {
                    Log.d("imgRecognitionFailure", "In UI Thread ${Looper.myLooper() == Looper.getMainLooper()}")
                    // Task failed with an exception
                    Log.e("imgRecognitionFailure", "Failed to processed $it", it)
                }
    }

    fun recognizeText(response: FirebaseVisionText): TweetMediaText {
        val resultText = response.text
        for (block in response.textBlocks) {
            val blockText = block.text
            val blockConfidence = block.confidence
            val blockLanguages = block.recognizedLanguages
            val blockCornerPoints = block.cornerPoints
            val blockFrame = block.boundingBox
            for (line in block.lines) {
                val lineText = line.text
                val lineConfidence = line.confidence
                val lineLanguages = line.recognizedLanguages
                val lineCornerPoints = line.cornerPoints
                val lineFrame = line.boundingBox
                for (element in line.elements) {
                    val elementText = element.text
                    val elementConfidence = element.confidence
                    val elementLanguages = element.recognizedLanguages
                    val elementCornerPoints = element.cornerPoints
                    val elementFrame = element.boundingBox
                }
            }
        }
        return TweetMediaText(text = response.text);
    }
}