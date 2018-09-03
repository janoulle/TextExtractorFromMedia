package com.janeullah.textextractorfrommedia.util

import android.content.Context
import android.net.Uri
import java.io.File


//https://developer.android.com/training/data-storage/files#WriteCacheFileInternal
// the following method extracts the file name from a URL and creates a file with that name in your app's internal cache directory:
fun getTempFile(context: Context, url: String): File? =
        Uri.parse(url)?.lastPathSegment?.let { filename ->
            File.createTempFile(filename, null, context.cacheDir)
        }