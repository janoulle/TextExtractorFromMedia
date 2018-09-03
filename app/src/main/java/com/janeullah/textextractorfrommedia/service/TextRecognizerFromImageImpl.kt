package com.janeullah.textextractorfrommedia.service

import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.janeullah.textextractorfrommedia.data.ImageInformation
import com.janeullah.textextractorfrommedia.data.TweetMediaText


fun recognizeText(response: FirebaseVisionText, tweetMediaItem: ImageInformation): TweetMediaText {
    val resultText = response.text
    return TweetMediaText(text = resultText, imageInfo = tweetMediaItem)
}