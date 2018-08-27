package com.janeullah.textextractorfrommedia.service

import android.graphics.Bitmap
import android.os.Looper
import android.util.Log
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentText
import com.janeullah.textextractorfrommedia.data.TweetMediaText


class TextRecognizerFromDocumentImpl : TextRecognizable {
    override fun recognizeText(image: Bitmap) {
        val mediaImage = FirebaseVisionImage.fromBitmap(image)
        val textRecognizer = FirebaseVision.getInstance()
                .cloudDocumentTextRecognizer
        textRecognizer.processImage(mediaImage)
                .addOnSuccessListener {
                    Log.d("docRecognitionSuccess", "In UI Thread ${Looper.myLooper() == Looper.getMainLooper()}")
                    val result = recognizeText(it)
                    Log.i("docRecognitionSuccess", result.toString())
                }
                .addOnFailureListener {
                    Log.d("docRecognitionFailure", "In UI Thread ${Looper.myLooper() == Looper.getMainLooper()}")
                    Log.e("docRecognitionFailure", "Failed to process $it", it)
                }
    }

    fun recognizeText(response: FirebaseVisionDocumentText): TweetMediaText {
        val resultText = response.text
        for (block in response.blocks) {
            val blockText = block.text
            val blockConfidence = block.confidence
            val blockRecognizedLanguages = block.recognizedLanguages
            val blockFrame = block.boundingBox
            for (paragraph in block.paragraphs) {
                val paragraphText = paragraph.text
                val paragraphConfidence = paragraph.confidence
                val paragraphRecognizedLanguages = paragraph.recognizedLanguages
                val paragraphFrame = paragraph.boundingBox
                for (word in paragraph.words) {
                    val wordText = word.text
                    val wordConfidence = word.confidence
                    val wordRecognizedLanguages = word.recognizedLanguages
                    val wordFrame = word.boundingBox
                    for (symbol in word.symbols) {
                        val symbolText = symbol.text
                        val symbolConfidence = symbol.confidence
                        val symbolRecognizedLanguages = symbol.recognizedLanguages
                        val symbolFrame = symbol.boundingBox
                    }
                }
            }
        }
        return TweetMediaText(text = response.text);
    }
}