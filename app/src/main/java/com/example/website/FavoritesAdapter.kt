package com.example.website

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class FavoritesAdapter(
    private val products: MutableList<Product>,
    private val onAddToCartClick: (Product) -> Unit,
    private val onRemoveFromFavorites: (Product) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    inner class FavoritesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productImage: ImageView = view.findViewById(R.id.productImage)
        val productName: TextView = view.findViewById(R.id.productName)
        val productPrice: TextView = view.findViewById(R.id.productPrice)
        val cartButton: ImageButton = view.findViewById(R.id.cartButton)
        val moreButton: ImageButton = view.findViewById(R.id.moreButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite_product, parent, false)
        return FavoritesViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val product = products[position]

        holder.productName.text = product.name
        holder.productPrice.text = String.format("%.2f zł", product.newPrice)

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

        holder.cartButton.setOnClickListener {
            showSizeDialog(context, product)
        }

        holder.moreButton.setOnClickListener { view ->
            showPopupMenu(view, product)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("product", product)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = products.size

    private fun showPopupMenu(view: View, product: Product) {
        val popup = PopupMenu(view.context, view)
        popup.menuInflater.inflate(R.menu.menu_favorite_item, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_remove -> {
                    onRemoveFromFavorites(product)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun showSizeDialog(context: android.content.Context, product: Product) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.size_picker_dialog, null)
        val numberPicker = dialogView.findViewById<NumberPicker>(R.id.sizePicker)
        numberPicker.minValue = 0
        numberPicker.maxValue = 9
        val sizes = (36..45).toList()
        numberPicker.displayedValues = sizes.map { it.toString() }.toTypedArray()

        AlertDialog.Builder(context)
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val selectedSize = sizes[numberPicker.value]
                product.selectedSize = selectedSize
                ProductData.addToCart(product)
                Toast.makeText(context, "Added to cart (size $selectedSize)", Toast.LENGTH_SHORT).show()
                onAddToCartClick(product)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
