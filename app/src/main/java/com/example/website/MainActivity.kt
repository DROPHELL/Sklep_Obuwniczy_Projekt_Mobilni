package com.example.website

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Отримуємо userId (email або інший унікальний ID)
        val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userId = prefs.getString("loggedInEmail", null) ?: "guest"

        // Ініціалізуємо сесію для цього користувача
        ProductData.init(applicationContext, userId)

        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.productRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val productList = ProductData.getAll()
        adapter = ProductAdapter(productList)
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
