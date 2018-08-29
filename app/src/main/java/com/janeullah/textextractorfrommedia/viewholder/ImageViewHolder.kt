package com.janeullah.textextractorfrommedia.viewholder

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.janeullah.textextractorfrommedia.R

// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder.
class ImageViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val imageView: ImageView

    init {
        // Define click listener for the ViewHolder's View.
        v.setOnClickListener {
            Log.d("viewHolder", "Element $adapterPosition clicked.")
            //sent to activity for processing the image
            //todo: DO SOMETHING
        }
        imageView = v.findViewById(R.id.imageView)
    }

}