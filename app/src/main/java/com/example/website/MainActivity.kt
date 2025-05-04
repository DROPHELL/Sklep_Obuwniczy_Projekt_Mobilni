package com.example.website

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ProductData.init(applicationContext)

        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.productRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val productList = ProductData.getAll()

        adapter = ProductAdapter(productList, onItemClick = { product ->
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("product", product)
            startActivity(intent)
        })

        recyclerView.adapter = adapter

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        val defaultTab = intent.getStringExtra("tab") ?: "home"
        when (defaultTab) {
            "home" -> bottomNavigationView.selectedItemId = R.id.menu_home
            "search" -> bottomNavigationView.selectedItemId = R.id.menu_search
            "favorites" -> bottomNavigationView.selectedItemId = R.id.menu_favorites
            "cart" -> bottomNavigationView.selectedItemId = R.id.menu_cart
            "profile" -> bottomNavigationView.selectedItemId = R.id.menu_profile
        }

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> true
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
    }
}
