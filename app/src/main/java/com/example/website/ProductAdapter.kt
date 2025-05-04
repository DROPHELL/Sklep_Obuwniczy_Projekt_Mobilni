package com.example.website

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class ProductAdapter(
    products: List<Product>,
    private val onItemClick: ((Product) -> Unit)? = null
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val items = products.toMutableList()

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        val productName: TextView = itemView.findViewById(R.id.productName)
        val productOldPrice: TextView = itemView.findViewById(R.id.productOldPrice)
        val productNewPrice: TextView = itemView.findViewById(R.id.productNewPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = items[position]

        holder.productName.text = product.name
        holder.productOldPrice.text = String.format(Locale.getDefault(), "%.2f zł", product.oldPrice)
        holder.productOldPrice.paintFlags = holder.productOldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.productNewPrice.text = String.format(Locale.getDefault(), "%.2f zł", product.newPrice)

        val context = holder.itemView.context
        val imageResId = context.resources.getIdentifier(
            product.imageNameBase + "1",
            "drawable",
            context.packageName
        )

        if (imageResId != 0) {
            holder.productImage.setImageResource(imageResId)
        } else {
            holder.productImage.setImageResource(R.drawable.shoe_placeholder)
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(product)
        }
    }
}
