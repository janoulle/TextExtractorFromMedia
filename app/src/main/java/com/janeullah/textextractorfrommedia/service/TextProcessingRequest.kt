package com.janeullah.textextractorfrommedia.service

import android.app.Activity
import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.janeullah.textextractorfrommedia.constants.RecognizableTypes
import com.janeullah.textextractorfrommedia.data.ImageInformation
import com.janeullah.textextractorfrommedia.task.ImageProcessingTask
import java.lang.ref.WeakReference

//listener here starts the async task for processing the bitmap
class TextProcessingRequest(val imageInfo: ImageInformation, val recognizableTypes: RecognizableTypes) {

    fun generateImageRequest(activity: Activity): ImageRequest {

        val listener: Response.Listener<Bitmap> = Response.Listener { response ->
            Log.i("imageDownloadSuccess", "Response: $response, type $recognizableTypes, imageUrl ${imageInfo.imageUrl}")
            ImageProcessingTask(WeakReference(activity), recognizableTypes).execute(response)
        }

        val errorListener = Response.ErrorListener { error ->
            Log.e("imageDownloadFailure", "type $recognizableTypes, imageUrl ${imageInfo.imageUrl}", error)
            Toast.makeText(activity, "Error fetching image from url ${imageInfo.imageUrl}", Toast.LENGTH_LONG).show()
        }

        val imageRequest = ImageRequest(
                imageInfo.imageUrl,
                listener,
                imageInfo.maxWidth,
                imageInfo.maxHeight,
                ImageView.ScaleType.FIT_CENTER,
                Bitmap.Config.RGB_565,
                errorListener
        )
        return imageRequest
    }
}