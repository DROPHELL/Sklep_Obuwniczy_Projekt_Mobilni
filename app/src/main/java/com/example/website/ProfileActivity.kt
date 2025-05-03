package com.example.website

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var emailTextView: TextView
    private lateinit var nameTextView: TextView
    private lateinit var walletButton: Button
    private lateinit var addressButton: Button
    private lateinit var logoutButton: Button
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Пошук елементів
        backButton = findViewById(R.id.backButton)
        emailTextView = findViewById(R.id.emailTextView)
        nameTextView = findViewById(R.id.nameTextView)
        walletButton = findViewById(R.id.walletButton)
        addressButton = findViewById(R.id.addressButton)
        logoutButton = findViewById(R.id.logoutButton)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val email = sharedPreferences.getString("loggedInEmail", "Not logged in") ?: "Not logged in"
        val name = sharedPreferences.getString("loggedInName", "Test User") ?: "Test User"

        nameTextView.text = "Name: $name"
        emailTextView.text = "Email Address: $email"

        // Кнопка назад
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("tab", "home")
            startActivity(intent)
            finish()
        }

        // Перехід до гаманця
        walletButton.setOnClickListener {
            startActivity(Intent(this, WalletActivity::class.java))
        }

        // Перехід до адрес
        addressButton.setOnClickListener {
            startActivity(Intent(this, AddressActivity::class.java))
        }

        // Вихід із акаунта
        logoutButton.setOnClickListener {
            sharedPreferences.edit().clear().putBoolean("isLoggedIn", false).apply()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Нижнє меню
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
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
                R.id.menu_cart -> {
                    startActivity(Intent(this, CartActivity::class.java))
                    finish()
                    true
                }
                R.id.menu_profile -> true
                else -> false
            }
        }

        bottomNavigationView.selectedItemId = R.id.menu_profile
    }
}
