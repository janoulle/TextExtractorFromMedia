package com.janeullah.textextractorfrommedia.util

import org.junit.Assert.*
import org.junit.Test

class StringHelperTest {

    @Test
    fun testIsValidLong_True() {
        assertTrue(StringHelper().isValidLong(Long.MAX_VALUE.toString()))
    }

    @Test
    fun testIsValidLong_FalseTooLarge() {
        assertFalse(StringHelper().isValidLong("10000000000000000001"))
    }

    @Test
    fun testIsValidLong_FalseStringNotLong() {
        assertFalse(StringHelper().isValidLong("abcde"))
    }

    @Test
    fun testExtractionOfTweetId_Success() {
        val stringHelper = StringHelper()
        assertEquals("1000000000000000001", stringHelper.extractTweetIdFromUrl("Check out this tweet: https://twitter.com/user/status/1000000000000000001?s=09"))
    }

    @Test
    fun testExtractionOfTweetId_ParameteNotSet() {
        val stringHelper = StringHelper()
        assertEquals("", stringHelper.extractTweetIdFromUrl(""))
        assertEquals("", stringHelper.extractTweetIdFromUrl(null))
    }

    @Test
    fun testExtractionOfTweetId_NoUrlFound() {
        val stringHelper = StringHelper()
        assertEquals("", stringHelper.extractTweetIdFromUrl("Check out this tweet"))
    }
}