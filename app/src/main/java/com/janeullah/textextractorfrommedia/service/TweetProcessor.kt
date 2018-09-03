package com.janeullah.textextractorfrommedia.service

import com.janeullah.textextractorfrommedia.data.ImageInformation
import com.twitter.sdk.android.core.models.MediaEntity


//https://medium.com/google-developer-experts/weakreference-in-android-dd1e66b9be9d
//https://kotlinlang.org/docs/reference/classes.html
//https://stackoverflow.com/questions/3243215/how-to-use-weakreference-in-java-and-android-development
//https://stackoverflow.com/questions/46797916/this-field-leaks-a-context-object
//https://github.com/facebook/facebook-android-sdk/blob/master/facebook-login/src/main/java/com/facebook/login/widget/ToolTipPopup.java
//https://community.oracle.com/blogs/enicholas/2006/05/04/understanding-weak-references
//https://stackoverflow.com/questions/3243215/how-to-use-weakreference-in-java-and-android-development
class TweetProcessor {

    //https://try.kotlinlang.org/#/Kotlin%20Koans/Collections/Filter%20map/Task.kt
    fun getMediaList(mediaEntities: List<MediaEntity>): ArrayList<ImageInformation> {
        val results =  mediaEntities
                .filter { it.type == "photo" }
                .map { ImageInformation(imageUrl = it.mediaUrlHttps, maxWidth = it.sizes.large.h, maxHeight = it.sizes.large.w) }
        return ArrayList(results)
    }

}