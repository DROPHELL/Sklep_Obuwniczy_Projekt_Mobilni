package com.example.website

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var emailTextView: TextView
    private lateinit var nameTextView: TextView
    private lateinit var shoeSizeTextView: TextView
    private lateinit var walletButton: Button
    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        backButton = findViewById(R.id.backButton)
        emailTextView = findViewById(R.id.emailTextView)
        nameTextView = findViewById(R.id.nameTextView)
        shoeSizeTextView = findViewById(R.id.shoeSizeTextView)
        walletButton = findViewById(R.id.walletButton)
        logoutButton = findViewById(R.id.logoutButton)

        // Отримання збережених даних користувача
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("loggedInEmail", "Not logged in") ?: "Not logged in"
        val name = sharedPreferences.getString("loggedInName", "Test User") ?: "Test User"
        val shoeSize = sharedPreferences.getString("shoeSize", "Not Set") ?: "Not Set"

        // Відображення даних у профілі
        nameTextView.text = "Name: $name"
        emailTextView.text = "Email Address: $email"
        shoeSizeTextView.text = "Shoe Size: $shoeSize"

        // Повернення на головний екран
        backButton.setOnClickListener {
            finish() // Закриває поточну активність і повертає на попередню
        }

        // Відкриття гаманця
        walletButton.setOnClickListener {
            startActivity(Intent(this, WalletActivity::class.java))
        }

        // Вихід з аккаунту
        logoutButton.setOnClickListener {
            logoutUser()
        }
    }

    private fun logoutUser() {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear() // Очищення даних користувача
        editor.putBoolean("isLoggedIn", false) // Скидання статусу авторизації
        editor.apply()

        // Перехід на екран логіну
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
