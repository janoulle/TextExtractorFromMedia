package com.janeullah.textextractorfrommedia.service

import android.content.Context
import android.graphics.Bitmap
import android.util.LruCache
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley

/**
 * https://developer.android.com/training/volley/requestqueue
 */
class NetworkRequestSingleton constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: NetworkRequestSingleton? = null

        fun getInstance(context: Context) =
                INSTANCE
                        ?: synchronized(this) {
                            INSTANCE
                                    ?: NetworkRequestSingleton(context)
                        }
    }

    val imageLoader: ImageLoader by lazy {
        ImageLoader(requestQueue,
                object : ImageLoader.ImageCache {
                    private val cache = LruCache<String, Bitmap>(20)
                    override fun getBitmap(url: String): Bitmap {
                        return cache.get(url)
                    }

                    override fun putBitmap(url: String, bitmap: Bitmap) {
                        cache.put(url, bitmap)
                    }
                })
    }
    val requestQueue: RequestQueue by lazy {
        // applicationContext is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }
}