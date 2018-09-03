package com.janeullah.textextractorfrommedia.data

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//https://kotlinlang.org/docs/reference/data-classes.html
//https://stackoverflow.com/questions/33551972/is-there-a-convenient-way-to-create-parcelable-data-classes-in-android-with-kotl
@Parcelize
data class TweetMediaText(val text: String = "", val imageInfo: ImageInformation, val bitMap: Bitmap? = null) : Parcelable