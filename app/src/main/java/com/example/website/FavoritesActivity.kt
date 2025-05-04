package com.example.website

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class FavoritesActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        backButton = findViewById(R.id.backButton)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        recyclerView = findViewById(R.id.favoritesRecyclerView)

        recyclerView.layoutManager = GridLayoutManager(this, 2)


        updateAdapter()

        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).putExtra("tab", "home"))
            finish()
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
                R.id.menu_favorites -> true
                R.id.menu_cart -> {
                    startActivity(Intent(this, CartActivity::class.java))
                    finish()
                    true
                }
                R.id.menu_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }

        bottomNavigationView.selectedItemId = R.id.menu_favorites
    }

    override fun onResume() {
        super.onResume()
        updateAdapter()
    }

    private fun updateAdapter() {
        val favoriteProducts = ProductData.getFavorites()

        adapter = FavoritesAdapter(
            favoriteProducts.toMutableList(),
            onAddToCartClick = { product ->
                ProductData.addToCart(product)
                Toast.makeText(this, "Added to Cart", Toast.LENGTH_SHORT).show()
            },
            onRemoveFromFavorites = { product ->
                ProductData.removeFromFavorites(product)
                updateAdapter()
                Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show()
            }
        )

        recyclerView.adapter = adapter
    }
}
