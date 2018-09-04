package com.janeullah.textextractorfrommedia

import com.janeullah.textextractorfrommedia.service.TweetProcessor
import com.twitter.sdk.android.core.models.MediaEntity
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TweetProcessorTest {

    @Test
    fun getMediaList_isSuccessful() {
        val mediaEntityList = listOf(MediaEntity("", "", "", 0, 0, 2342342L, "2342342",
                "http://pbs.twimg.com/media/DlsHtRkXcAEmNXt.jpg", "https://pbs.twimg.com/media/DlsHtRkXcAEmNXt.jpg",
                MediaEntity.Sizes(MediaEntity.Size(10, 10, ""), MediaEntity.Size(20, 20, ""), MediaEntity.Size(50, 50, ""), MediaEntity.Size(100, 100, "")),
                0L, "", "photo", null, ""))
        val expectedTransformedResult = TweetProcessor("20").getMediaList(mediaEntityList)
        assertEquals(1, expectedTransformedResult.size)
        assertEquals(100, expectedTransformedResult[0].maxHeight)
        assertEquals(100, expectedTransformedResult[0].maxWidth)
        assertEquals("https://pbs.twimg.com/media/DlsHtRkXcAEmNXt.jpg", expectedTransformedResult[0].imageUrl)
    }
}