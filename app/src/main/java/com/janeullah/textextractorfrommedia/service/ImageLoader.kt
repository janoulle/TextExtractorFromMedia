package com.janeullah.textextractorfrommedia.service

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.support.annotation.DrawableRes
import android.widget.ImageView
import java.io.File


interface ImageLoader {
    /**
     * Fetches the image (using Glide) from the provided path and loads into the image view
     */
    fun load(context: Context,
             path: String,
             @DrawableRes placeholder: Int,
             imageView: ImageView)

    /**
     * Writes the provided bitmap to the Android cache dir, updates the image information object
     * with the file details
     */
    fun writeToTempFolder(context: Context, bitmap: Bitmap, path: String) : File?

    /**
     * https://developer.android.com/training/data-storage/files#WriteCacheFileInternal
     * the following method extracts the file name from a URL and creates a file with that name in your app's internal cache directory
     */
    fun getTempFile(context: Context, url: String): File? =
            Uri.parse(url)?.lastPathSegment?.let { filename ->
                File.createTempFile(filename, null, context.cacheDir)
            }
}