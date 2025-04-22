package com.example.website

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(
    products: List<Product>,
    private val isCartScreen: Boolean = false,
    private val isFavoritesScreen: Boolean = false,
    private val onItemClick: ((Product) -> Unit)? = null
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val items = products.toMutableList()

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        val productName: TextView = itemView.findViewById(R.id.productName)
        val productOldPrice: TextView = itemView.findViewById(R.id.productOldPrice)
        val productNewPrice: TextView = itemView.findViewById(R.id.productNewPrice)
        val favoriteButton: ImageButton = itemView.findViewById(R.id.favoriteButton)
        val cartButton: ImageButton = itemView.findViewById(R.id.cartButton)
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
        holder.productOldPrice.text = String.format("$%.2f", product.oldPrice)
        holder.productNewPrice.text = String.format("$%.2f", product.newPrice)
        holder.productImage.setImageResource(product.imageResId)

        holder.favoriteButton.setImageResource(
            if (ProductData.isFavorite(product)) R.drawable.ic_favorite_full
            else R.drawable.ic_favorite
        )
        holder.cartButton.setImageResource(
            if (ProductData.isInCart(product)) R.drawable.ic_cart_full
            else R.drawable.ic_cart_vector
        )

        holder.favoriteButton.visibility = View.VISIBLE
        holder.cartButton.visibility = View.VISIBLE

        // Видалення з Favorites
        if (isFavoritesScreen) {
            holder.favoriteButton.setOnClickListener {
                ProductData.removeFromFavorites(product)
                items.removeAt(position)
                notifyItemRemoved(position)
            }
        } else {
            holder.favoriteButton.setOnClickListener {
                if (ProductData.isFavorite(product)) {
                    ProductData.removeFromFavorites(product)
                } else {
                    ProductData.addToFavorites(product)
                }
                notifyItemChanged(position)
            }
        }

        // Видалення з Cart
        if (isCartScreen) {
            holder.cartButton.setOnClickListener {
                ProductData.removeFromCart(product)
                items.removeAt(position)
                notifyItemRemoved(position)
            }
        } else {
            holder.cartButton.setOnClickListener {
                if (ProductData.isInCart(product)) {
                    ProductData.removeFromCart(product)
                } else {
                    ProductData.addToCart(product)
                }
                notifyItemChanged(position)
            }
        }

        // 👇 Переходити до ProductDetailActivity
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(product)
        }
    }
}
