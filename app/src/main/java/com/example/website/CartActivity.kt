package com.example.website

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class CartActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var recyclerView: RecyclerView
    private lateinit var checkoutButton: Button
    private lateinit var adapter: CartAdapter
    private var cartItems = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        backButton = findViewById(R.id.backButton)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        recyclerView = findViewById(R.id.cartRecyclerView)
        checkoutButton = findViewById(R.id.checkoutButton)

        cartItems = ProductData.getCart().toMutableList()
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = CartAdapter(
            products = cartItems,
            onRemoveFromCart = { product ->
                ProductData.removeFromCart(product)
                cartItems.remove(product)
                adapter.notifyDataSetChanged()
                updateTotalPrice()
                Toast.makeText(this, "Removed from cart", Toast.LENGTH_SHORT).show()
            }
        )

        recyclerView.adapter = adapter
        updateTotalPrice()

        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).putExtra("tab", "home"))
            finish()
        }

        checkoutButton.setOnClickListener {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "The cart is empty!", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, CheckoutActivity::class.java)
                startActivity(intent)
            }
        }

        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> {
                    startActivity(Intent(this, MainActivity::class.java).putExtra("tab", "home"))
                    finish()
                    true
                }
                R.id.menu_search -> {
                    startActivity(Intent(this, SearchActivity::class.java))
                    finish()
                    true
                }
                R.id.menu_favorites -> {
                    startActivity(Intent(this, FavoritesActivity::class.java))
                    finish()
                    true
                }
                R.id.menu_cart -> true
                R.id.menu_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }

        bottomNavigationView.selectedItemId = R.id.menu_cart
    }

    private fun updateTotalPrice() {
        val total = cartItems.sumOf { it.newPrice }
        checkoutButton.text = String.format("TO CASH • %.2f zł", total)
    }
}
