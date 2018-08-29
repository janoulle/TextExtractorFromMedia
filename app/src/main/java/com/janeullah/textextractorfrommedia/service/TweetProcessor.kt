package com.janeullah.textextractorfrommedia.service

import android.app.Activity
import com.janeullah.textextractorfrommedia.constants.RecognizableTypes
import com.janeullah.textextractorfrommedia.data.ImageInformation
import com.janeullah.textextractorfrommedia.task.ImageDownloadTask
import com.twitter.sdk.android.core.models.MediaEntity
import java.lang.ref.WeakReference


class TweetProcessor {

    fun processTweetMedia(mediaEntities: List<MediaEntity>, activity: Activity, recognizer: RecognizableTypes) {

        for (mediaEntity: MediaEntity in mediaEntities) {

            val imageUrl = mediaEntity.mediaUrlHttps
            val maxHeight = mediaEntity.sizes.large.h
            val maxWidth = mediaEntity.sizes.large.w

            val processingRequest = TextProcessingRequest(
                    ImageInformation(imageUrl = imageUrl, maxWidth = maxWidth, maxHeight = maxHeight),
                    recognizer)
            ImageDownloadTask(WeakReference(activity)).execute(processingRequest)

        }

    }

    //https://try.kotlinlang.org/#/Kotlin%20Koans/Collections/Filter%20map/Task.kt
    fun getMediaList(mediaEntities: List<MediaEntity>): ArrayList<ImageInformation> {
        val results =  mediaEntities
                .filter { it.type == "photo" }
                .map { ImageInformation(imageUrl = it.mediaUrlHttps, maxWidth = it.sizes.large.h, maxHeight = it.sizes.large.w) }
        return ArrayList(results)
    }

}