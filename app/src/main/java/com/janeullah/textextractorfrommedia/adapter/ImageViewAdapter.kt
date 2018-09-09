package com.janeullah.textextractorfrommedia.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.janeullah.textextractorfrommedia.R
import com.janeullah.textextractorfrommedia.data.ImageInformation
import com.janeullah.textextractorfrommedia.viewholder.ImageViewHolder


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
        val tweetMediaItem = tweetMedia[position]

        holder.populateImage(tweetMediaItem)

        holder.setOnClickListener(tweetMediaItem, tweetMedia)

    }

}