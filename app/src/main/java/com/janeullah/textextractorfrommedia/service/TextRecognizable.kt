package com.janeullah.textextractorfrommedia.service

import android.graphics.Bitmap

interface TextRecognizable {
    fun recognizeText(image: Bitmap)
}