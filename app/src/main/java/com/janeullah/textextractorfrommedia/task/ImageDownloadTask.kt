package com.janeullah.textextractorfrommedia.task

import android.app.Activity
import android.os.AsyncTask
import android.os.Looper
import android.util.Log
import com.janeullah.textextractorfrommedia.service.NetworkRequestSingleton
import com.janeullah.textextractorfrommedia.service.TextProcessingRequest
import java.lang.ref.WeakReference

//Downloading the image using volley
class ImageDownloadTask(val context: WeakReference<Activity>) : AsyncTask<TextProcessingRequest, Void, Void>() {

    override fun doInBackground(vararg processingRequests: TextProcessingRequest?): Void? {
        Log.d("imgDownloadTask", "In UI Thread ${Looper.myLooper() == Looper.getMainLooper()}")
        val activity = context.get()
        activity?.let {
            Log.i("imgDownloadTask", "In UI Thread ${Looper.myLooper() == Looper.getMainLooper()}")
            if (!processingRequests.isEmpty()) {
                val imageRequest = processingRequests[0]?.generateImageRequest(it)
                imageRequest?.let { request ->
                    NetworkRequestSingleton.getInstance(it).addToRequestQueue(request)
                }
            }
        }
        return null
    }

}