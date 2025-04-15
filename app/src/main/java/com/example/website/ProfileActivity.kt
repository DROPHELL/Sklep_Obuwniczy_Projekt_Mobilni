package com.example.website

import android.content.Context
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
    private lateinit var logoutButton: Button
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var shoeSizeSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Ініціалізація елементів
        backButton = findViewById(R.id.backButton)
        emailTextView = findViewById(R.id.emailTextView)
        nameTextView = findViewById(R.id.nameTextView)
        walletButton = findViewById(R.id.walletButton)
        logoutButton = findViewById(R.id.logoutButton)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        shoeSizeSpinner = findViewById(R.id.shoeSizeSpinner)

        // SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("loggedInEmail", "Not logged in") ?: "Not logged in"
        val name = sharedPreferences.getString("loggedInName", "Test User") ?: "Test User"
        val savedSize = sharedPreferences.getString("shoeSize", "Not Set")

        nameTextView.text = "Name: $name"
        emailTextView.text = "Email Address: $email"

        // Заповнення Spinner-а з розмірами
        val sizes = listOf("Not Set", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sizes)
        shoeSizeSpinner.adapter = adapter

        // Встановити вибраний розмір
        val index = sizes.indexOf(savedSize)
        if (index >= 0) shoeSizeSpinner.setSelection(index)

        // Зберігати обраний розмір
        shoeSizeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                val selectedSize = sizes[position]
                sharedPreferences.edit().putString("shoeSize", selectedSize).apply()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Назад
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("tab", "home")
            startActivity(intent)
            finish()
        }

        // Wallet
        walletButton.setOnClickListener {
            startActivity(Intent(this, WalletActivity::class.java))
        }

        // Log Out
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
