package com.janeullah.textextractorfrommedia.adapter

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.janeullah.textextractorfrommedia.R
import com.janeullah.textextractorfrommedia.activity.AnalyzedImageActivity
import com.janeullah.textextractorfrommedia.constants.IntentNames
import com.janeullah.textextractorfrommedia.constants.RecognizableTypes
import com.janeullah.textextractorfrommedia.data.ImageInformation
import com.janeullah.textextractorfrommedia.module.GlideApp
import com.janeullah.textextractorfrommedia.util.getTempFile
import com.janeullah.textextractorfrommedia.viewholder.ImageViewHolder
import kotlinx.android.synthetic.main.image_row_item.view.*
import java.io.FileOutputStream


class ImageViewAdapter(private val activity: Activity, private var tweetMedia: ArrayList<ImageInformation>) : RecyclerView.Adapter<ImageViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {// create a new view
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.image_row_item, parent, false)
        // set the view's size, margins, paddings and layout parameters

        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tweetMedia.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val tweetMediaItem = tweetMedia.get(position)

        //https://github.com/TWiStErRob/glide-support/blob/master/src/glide4/java/com/bumptech/glide/supportapp/random/__quicky/QuickFragment.java
        //https://github.com/bumptech/glide/wiki/Custom-targets
        //https://github.com/TWiStErRob/glide-support/blob/master/src/main/java/com/bumptech/glide/supportapp/GlideRecyclerFragment.java
        //https://github.com/TWiStErRob/glide-support/tree/master/src/main/java/com/bumptech/glide/supportapp
        //https://github.com/bumptech/glide/issues/428
        GlideApp.with(activity)
                .load(tweetMediaItem.imageUrl)
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
                .into(holder.imageView)

        holder.imageView.setOnClickListener {

            val bitMap : Bitmap = (it.imageView.drawable as BitmapDrawable).bitmap

            val tempFile = getTempFile(it.context,tweetMediaItem.imageUrl)

            tempFile?.let {file ->

                /*it.context.openFileOutput(tempFile.name, Context.MODE_PRIVATE).use {
                    bitMap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                }*/

                val outputStream = FileOutputStream(file)
                bitMap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()

                //capture location
                tweetMediaItem.cachedUrlName = file.name ?: ""
                tweetMediaItem.cachedAbsolutePath = file.absolutePath ?: ""

                val intent = Intent(activity, AnalyzedImageActivity::class.java).apply {
                    putExtra(IntentNames.PARSE_MODE, RecognizableTypes.IMAGE.text)
                    putExtra(IntentNames.TWEET_MEDIA_ITEM, tweetMediaItem)
                }
                activity.startActivity(intent)

            }

        }
    }

}