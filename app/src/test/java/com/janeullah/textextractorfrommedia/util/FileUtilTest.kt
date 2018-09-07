package com.janeullah.textextractorfrommedia.util

import android.content.Context
import com.janeullah.textextractorfrommedia.BuildConfig
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File

//http://alexzh.com/tutorials/android-testing-mockito-robolectric/
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = [21])
class FileUtilTest {

    @Mock
    private lateinit var mMockContext: Context

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    private lateinit var tempFile : File

    @Before
    fun setUpMockito() {
        MockitoAnnotations.initMocks(this)
        tempFile = temporaryFolder.newFile("dummybitmap.jpg")
    }

    @After
    fun tearDownMockito() {
        Mockito.validateMockitoUsage()
    }

    @Test
    fun testCreationOfTempFile() {
        assertEquals(null, getTempFile(mMockContext, "https://pbs.twimg.com/media/DlsHtRkXcAEmNXt.jpg"))
    }
}