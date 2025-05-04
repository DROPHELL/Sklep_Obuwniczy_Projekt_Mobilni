package com.example.website

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(
    private val products: MutableList<Product>,
    private val onRemoveFromCart: (Product) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cartProductImage: ImageView = view.findViewById(R.id.cartProductImage)
        val cartProductName: TextView = view.findViewById(R.id.cartProductName)
        val cartProductPrice: TextView = view.findViewById(R.id.cartProductPrice)
        val cartProductSize: TextView = view.findViewById(R.id.cartProductSize)
        val cartMoreButton: ImageButton = view.findViewById(R.id.cartMoreButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart_product, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = products[position]

        holder.cartProductName.text = product.name
        holder.cartProductPrice.text = String.format("%.2f zł", product.newPrice)


        if (product.selectedSize != null) {
            holder.cartProductSize.text = "Size: ${product.selectedSize}"
        } else {
            holder.cartProductSize.text = "Size: -"
        }

        val context = holder.itemView.context
        val imageResId = context.resources.getIdentifier(
            product.imageNameBase + "1",
            "drawable",
            context.packageName
        )
        if (imageResId != 0) {
            holder.cartProductImage.setImageResource(imageResId)
        } else {
            holder.cartProductImage.setImageResource(R.drawable.shoe_placeholder)
        }

        holder.cartMoreButton.setOnClickListener {
            showPopupMenu(it, product)
        }
    }

    private fun showPopupMenu(view: View, product: Product) {
        val popup = PopupMenu(view.context, view)
        popup.menuInflater.inflate(R.menu.menu_cart_item, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.menu_remove_cart) {
                onRemoveFromCart(product)
                true
            } else {
                false
            }
        }
        popup.show()
    }

    override fun getItemCount(): Int = products.size
}
