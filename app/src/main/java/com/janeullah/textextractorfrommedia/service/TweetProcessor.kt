package com.janeullah.textextractorfrommedia.service

import android.app.Activity
import com.janeullah.textextractorfrommedia.constants.RecognizableTypes
import com.janeullah.textextractorfrommedia.task.ImageDownloadTask
import com.twitter.sdk.android.core.models.MediaEntity
import java.lang.ref.WeakReference


class TweetProcessor {

    fun processTweetMedia(mediaEntities: List<MediaEntity>, activity: Activity, recognizer: RecognizableTypes) {

        for (mediaEntity: MediaEntity in mediaEntities) {

            val imageUrl = mediaEntity.mediaUrlHttps
            val maxHeight = mediaEntity.sizes.large.h
            val maxWidth = mediaEntity.sizes.large.w

            val processingRequest = TextProcessingRequest(imageUrl, maxWidth, maxHeight, recognizer)
            ImageDownloadTask(WeakReference(activity)).execute(processingRequest)

        }

    }

}