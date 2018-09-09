package com.janeullah.textextractorfrommedia.viewholder

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.janeullah.textextractorfrommedia.R
import com.janeullah.textextractorfrommedia.activity.AnalyzedImageActivity
import com.janeullah.textextractorfrommedia.constants.IntentNames
import com.janeullah.textextractorfrommedia.constants.RecognizableTypes
import com.janeullah.textextractorfrommedia.data.ImageInformation
import com.janeullah.textextractorfrommedia.service.ImageLoaderImpl
import kotlinx.android.synthetic.main.image_row_item.view.*

// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder.
class ImageViewHolder(v: View, private val imageLoader: ImageLoaderImpl = ImageLoaderImpl()) : RecyclerView.ViewHolder(v) {
    val imageView: ImageView = v.findViewById(R.id.imageView)
    val context: Context = v.context

    fun populateImage(imageInformation: ImageInformation) {
        imageLoader.load(context, imageInformation.imageUrl, 0, imageView)
    }

    fun setOnClickListener(tweetMediaItem: ImageInformation, tweetMediaList: ArrayList<ImageInformation>) {
        imageView.setOnClickListener {

            val bitMap : Bitmap = (it.imageView.drawable as BitmapDrawable).bitmap

            val file = imageLoader.writeToTempFolder(it.context, bitMap, tweetMediaItem.imageUrl)

            file?.let { savedFile ->
                tweetMediaItem.cachedUrlName = savedFile.name ?: ""
                tweetMediaItem.cachedAbsolutePath = savedFile.absolutePath ?: ""

                val intent = Intent(it.context, AnalyzedImageActivity::class.java).apply {
                    putExtra(IntentNames.PARSE_MODE, RecognizableTypes.IMAGE.text)
                    putExtra(IntentNames.TWEET_MEDIA_ITEM, tweetMediaItem)
                    //setting TWEET_MEDIA_LIST to support going back via the home icon
                    putParcelableArrayListExtra(IntentNames.TWEET_MEDIA_LIST, tweetMediaList)
                }
                it.context.startActivity(intent)
            }
        }
    }

}