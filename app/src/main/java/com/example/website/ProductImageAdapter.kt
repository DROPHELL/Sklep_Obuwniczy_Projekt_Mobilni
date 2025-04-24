package com.example.website

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ProductImageAdapter(
    private val images: List<Int>,
    private val onImageClick: (Int) -> Unit
) : RecyclerView.Adapter<ProductImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val thumbnail: ImageView = itemView.findViewById(R.id.thumbnailImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_thumbnail, parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.thumbnail.setImageResource(images[position])
        holder.thumbnail.setOnClickListener {
            onImageClick(images[position])
        }
    }
}
