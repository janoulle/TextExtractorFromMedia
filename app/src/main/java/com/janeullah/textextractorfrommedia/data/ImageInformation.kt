package com.janeullah.textextractorfrommedia.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageInformation(val imageUrl: String = "", val maxHeight : Int, val maxWidth : Int, val cropWidth : Int = 100, val cropHeight : Int = 100, var cachedUrlName : String = "", var cachedAbsolutePath : String = "") : Parcelable
//    @IgnoredOnParcel
//    var drawable: Drawable? = null
