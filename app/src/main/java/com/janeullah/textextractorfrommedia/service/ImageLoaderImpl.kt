package com.janeullah.textextractorfrommedia.service

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.janeullah.textextractorfrommedia.R
import com.janeullah.textextractorfrommedia.module.GlideApp
import java.io.File
import java.io.FileOutputStream

class ImageLoaderImpl : ImageLoader {

    override fun writeToTempFolder(context: Context, bitmap: Bitmap, path: String): File? {
        val tempFile = getTempFile(context, path)

        tempFile?.let { file ->

            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            with(outputStream) {
                flush()
                close()
            }
        }

        return tempFile
    }

    override fun load(context: Context, path: String, placeholder: Int, imageView: ImageView) {
        //https://github.com/TWiStErRob/glide-support/blob/master/src/glide4/java/com/bumptech/glide/supportapp/random/__quicky/QuickFragment.java
        //https://github.com/bumptech/glide/wiki/Custom-targets
        //https://github.com/TWiStErRob/glide-support/blob/master/src/main/java/com/bumptech/glide/supportapp/GlideRecyclerFragment.java
        //https://github.com/TWiStErRob/glide-support/tree/master/src/main/java/com/bumptech/glide/supportapp
        //https://github.com/bumptech/glide/issues/428
        GlideApp.with(context)
                .load(path)
                .apply(RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .fitCenter()
                        //.placeholder(R.drawable.glide_placeholder) //placeholder size can mess with the image display. smh
                        //.fallback(R.drawable.glide_fallback)
                        .error(R.drawable.glide_error)
                )
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>,
                                              isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>,
                                                 dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        //do something with the bitmap if you so desire!
                        //tweetMediaItem.drawable = resource
                        return false
                    }
                })
                .into(imageView)
    }
}