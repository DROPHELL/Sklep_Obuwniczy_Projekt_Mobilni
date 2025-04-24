package com.example.website

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var mainImage: ImageView
    private lateinit var thumbnailContainer: LinearLayout
    private lateinit var productName: TextView
    private lateinit var productPrice: TextView
    private lateinit var sizeContainer: FlexboxLayout
    private lateinit var quantityText: TextView
    private lateinit var buyButton: MaterialButton

    private lateinit var selectedProduct: Product
    private var selectedSize: String? = null
    private var quantity = 1

    private val sizes = listOf("36", "37", "38", "39", "40", "41", "42", "43", "44", "45")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.menu_home
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> startActivity(Intent(this, MainActivity::class.java))
                R.id.menu_search -> startActivity(Intent(this, SearchActivity::class.java))
                R.id.menu_favorites -> startActivity(Intent(this, FavoritesActivity::class.java))
                R.id.menu_cart -> startActivity(Intent(this, CartActivity::class.java))
                R.id.menu_profile -> startActivity(Intent(this, ProfileActivity::class.java))
            }
            finish()
            true
        }

        mainImage = findViewById(R.id.productImage)
        thumbnailContainer = findViewById(R.id.thumbnailContainer)
        productName = findViewById(R.id.productName)
        productPrice = findViewById(R.id.productPrice)
        sizeContainer = findViewById(R.id.sizeContainer)
        quantityText = findViewById(R.id.quantityText)
        buyButton = findViewById(R.id.buyButton)

        val minusButton = findViewById<MaterialButton>(R.id.minusButton)
        val plusButton = findViewById<MaterialButton>(R.id.plusButton)

        selectedProduct = intent.getSerializableExtra("product") as Product
        val baseName = selectedProduct.imageNameBase

        productName.text = selectedProduct.name
        productPrice.text = "Cena: %.2f zł".format(selectedProduct.newPrice)

        val mainResId = resources.getIdentifier(baseName + "1", "drawable", packageName)
        mainImage.setImageResource(mainResId)

        for (i in 1..4) {
            val resId = resources.getIdentifier(baseName + i, "drawable", packageName)
            val thumb = ImageView(this)
            thumb.setImageResource(resId)

            val size = resources.getDimensionPixelSize(R.dimen.thumbnail_size)
            val margin = resources.getDimensionPixelSize(R.dimen.thumbnail_margin)
            val params = LinearLayout.LayoutParams(size, size)
            params.setMargins(margin, 0, margin, 0)
            thumb.layoutParams = params

            thumb.setOnClickListener {
                mainImage.setImageResource(resId)
            }

            thumbnailContainer.addView(thumb)
        }

        val savedSize = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            .getString("shoeSize", null)

        sizes.forEach { size ->
            val button = MaterialButton(this)
            button.text = size
            button.setPadding(24, 8, 24, 8)
            button.setTextSize(14f)
            button.setTypeface(null, Typeface.BOLD)
            button.setBackgroundColor(getColor(android.R.color.transparent))
            button.setStrokeColorResource(R.color.black)
            button.strokeWidth = 2
            button.setTextColor(getColor(R.color.black))
            button.cornerRadius = 24

            val layoutParams = FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(8, 8, 8, 8)
            button.layoutParams = layoutParams

            if (savedSize == size) {
                selectSize(button)
                selectedSize = size
            }

            button.setOnClickListener {
                selectSize(button)
                selectedSize = size
            }

            sizeContainer.addView(button)
        }

        quantityText.text = quantity.toString()

        minusButton.setOnClickListener {
            if (quantity > 1) {
                quantity--
                quantityText.text = quantity.toString()
            }
        }

        plusButton.setOnClickListener {
            quantity++
            quantityText.text = quantity.toString()
        }

        buyButton.setOnClickListener {
            if (selectedSize == null) {
                Toast.makeText(this, "Wybierz rozmiar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(
                this,
                "Zakupiono: ${selectedProduct.name}, rozmiar $selectedSize x$quantity",
                Toast.LENGTH_SHORT
            ).show()

            // Тут можна додати перехід до екрана оплати
        }
    }

    private fun selectSize(selected: MaterialButton) {
        for (i in 0 until sizeContainer.childCount) {
            val child = sizeContainer.getChildAt(i)
            if (child is MaterialButton) {
                child.setBackgroundColor(getColor(android.R.color.transparent))
                child.setTextColor(getColor(R.color.black))
            }
        }
        selected.setBackgroundColor(getColor(R.color.black))
        selected.setTextColor(getColor(android.R.color.white))
    }
}
