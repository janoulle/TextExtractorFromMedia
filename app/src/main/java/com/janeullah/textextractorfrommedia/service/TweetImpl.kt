package com.janeullah.textextractorfrommedia.service

import android.app.Activity
import com.janeullah.textextractorfrommedia.constants.RecognizableTypes
import com.twitter.sdk.android.core.models.MediaEntity
import com.twitter.sdk.android.core.models.Tweet


class TweetImpl : Tweetable {

    override fun processTweet(tweet: Tweet?, activity: Activity, recognizer: RecognizableTypes) {

        val mediaEntities = tweet?.extendedEntities?.media ?: listOf()

        for (mediaEntity: MediaEntity in mediaEntities) {

            val imageUrl = mediaEntity.mediaUrlHttps
            val maxHeight = mediaEntity.sizes.large.h
            val maxWidth = mediaEntity.sizes.large.w

            val imageRequest = generateImageRequest(imageUrl, maxWidth, maxHeight, activity, recognizer)

            NetworkRequestSingleton.getInstance(activity).addToRequestQueue(imageRequest)
        }

    }

}