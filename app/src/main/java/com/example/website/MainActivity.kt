package com.example.website

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Отримання вкладки (за замовчуванням Home)
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
                R.id.menu_home -> {
                    Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_search -> {
                    startActivity(Intent(this, SearchActivity::class.java))
                    true
                }
                R.id.menu_favorites -> {
                    startActivity(Intent(this, FavoritesActivity::class.java))
                    true
                }
                R.id.menu_cart -> {
                    startActivity(Intent(this, CartActivity::class.java))
                    true
                }
                R.id.menu_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
