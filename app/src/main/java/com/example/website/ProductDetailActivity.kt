package com.example.website

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.button.MaterialButton

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var productImage: ImageView
    private lateinit var thumbnailContainer: LinearLayout
    private lateinit var productName: TextView
    private lateinit var productPrice: TextView
    private lateinit var sizeContainer: FlexboxLayout
    private lateinit var buyButton: MaterialButton
    private lateinit var favoriteButton: ImageButton

    private var selectedProduct: Product? = null
    private var selectedSize: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        productImage = findViewById(R.id.productImage)
        thumbnailContainer = findViewById(R.id.thumbnailContainer)
        productName = findViewById(R.id.productName)
        productPrice = findViewById(R.id.productPrice)
        sizeContainer = findViewById(R.id.sizeContainer)
        buyButton = findViewById(R.id.buyButton)
        favoriteButton = findViewById(R.id.favoriteButton)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        selectedProduct = intent.getSerializableExtra("product") as? Product

        // ➔ Взяти розмір з профілю автоматично
        val sessionManager = SessionManager(this)
        selectedSize = sessionManager.getShoeSize()

        selectedProduct?.let { product ->
            val resId = resources.getIdentifier(product.imageNameBase + "1", "drawable", packageName)
            productImage.setImageResource(resId.takeIf { it != 0 } ?: R.drawable.shoe_placeholder)

            productName.text = product.name
            productPrice.text = "Cena: %.2f zł".format(product.newPrice)

            addThumbnails(product)
            addSizeOptions() // Додаємо розміри
            updateFavoriteIcon()
        }

        buyButton.setOnClickListener {
            selectedProduct?.let { product ->
                if (selectedSize == null) {
                    Toast.makeText(this, "Wybierz rozmiar!", Toast.LENGTH_SHORT).show()
                } else {
                    product.selectedSize = selectedSize
                    ProductData.addToCart(product)
                    Toast.makeText(this, "Dodano do koszyka!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, CartActivity::class.java))
                    finish()
                }
            }
        }

        favoriteButton.setOnClickListener {
            selectedProduct?.let { product ->
                if (ProductData.isFavorite(product)) {
                    ProductData.removeFromFavorites(product)
                    Toast.makeText(this, "Usunięto z ulubionych", Toast.LENGTH_SHORT).show()
                } else {
                    ProductData.addToFavorites(product)
                    Toast.makeText(this, "Dodano do ulubionych!", Toast.LENGTH_SHORT).show()
                }
                updateFavoriteIcon()
            }
        }
    }

    private fun addThumbnails(product: Product) {
        thumbnailContainer.removeAllViews()
        for (i in 1..4) {
            val thumb = ImageView(this)
            val resId = resources.getIdentifier(product.imageNameBase + "$i", "drawable", packageName)
            thumb.setImageResource(resId.takeIf { it != 0 } ?: R.drawable.shoe_placeholder)
            val params = LinearLayout.LayoutParams(180, 180).apply {
                setMargins(8, 8, 8, 8)
            }
            thumb.layoutParams = params
            thumb.scaleType = ImageView.ScaleType.CENTER_CROP
            thumb.setOnClickListener {
                productImage.setImageResource(resId)
            }
            thumbnailContainer.addView(thumb)
        }
    }

    private fun addSizeOptions() {
        val sizes = listOf(36, 37, 38, 39, 40, 41, 42, 43, 44, 45)
        sizeContainer.removeAllViews()

        for (size in sizes) {
            val button = MaterialButton(this).apply {
                text = size.toString()
                setBackgroundColor(resources.getColor(android.R.color.white))
                setTextColor(resources.getColor(R.color.black))
                strokeWidth = 2
                strokeColor = getColorStateList(R.color.black)
                cornerRadius = 12
                layoutParams = FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(8, 8, 8, 8)
                }
                setOnClickListener {
                    selectedSize = size
                    highlightSelectedSize(this)
                }
            }
            sizeContainer.addView(button)

            // Якщо розмір співпадає з збереженим, підсвічуємо
            if (size == selectedSize) {
                highlightSelectedSize(button)
            }
        }
    }

    private fun highlightSelectedSize(selectedButton: MaterialButton) {
        for (i in 0 until sizeContainer.childCount) {
            val button = sizeContainer.getChildAt(i) as MaterialButton
            button.setBackgroundColor(resources.getColor(android.R.color.white))
            button.setTextColor(resources.getColor(R.color.black))
        }
        selectedButton.setBackgroundColor(resources.getColor(R.color.black))
        selectedButton.setTextColor(resources.getColor(android.R.color.white))
    }

    private fun updateFavoriteIcon() {
        selectedProduct?.let { product ->
            if (ProductData.isFavorite(product)) {
                favoriteButton.setImageResource(R.drawable.ic_favorite_full)
            } else {
                favoriteButton.setImageResource(R.drawable.ic_favorite)
            }
        }
    }
}
