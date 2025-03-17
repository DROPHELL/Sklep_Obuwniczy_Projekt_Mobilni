package com.example.website

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class CartActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        backButton = findViewById(R.id.backButton)

        backButton.setOnClickListener {
            // Повернення на головний екран
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
