package com.example.website

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class CartActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        backButton = findViewById(R.id.backButton)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Назад → перехід до MainActivity з Home
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("tab", "home")
            startActivity(intent)
            finish()
        }

        // Обробка нижнього меню
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("tab", "home")
                    startActivity(intent)
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
                R.id.menu_cart -> true // вже тут
                R.id.menu_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }

        // Виділяємо активну вкладку
        bottomNavigationView.selectedItemId = R.id.menu_cart
    }
}
