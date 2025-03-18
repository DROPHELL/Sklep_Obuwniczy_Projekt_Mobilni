package com.example.website

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class WalletActivity : AppCompatActivity() {

    private lateinit var walletBalanceTextView: TextView
    private lateinit var cardNumberEditText: EditText
    private lateinit var cvvEditText: EditText
    private lateinit var expiryDateEditText: EditText
    private lateinit var saveCardButton: Button
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)

        walletBalanceTextView = findViewById(R.id.walletBalanceTextView)
        cardNumberEditText = findViewById(R.id.cardNumberEditText)
        cvvEditText = findViewById(R.id.cvvEditText)
        expiryDateEditText = findViewById(R.id.expiryDateEditText)
        saveCardButton = findViewById(R.id.saveCardButton)
        backButton = findViewById(R.id.backButton)

        // Отримання даних з SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val balance = sharedPreferences.getFloat("walletBalance", 0.0f)
        val savedCardNumber = sharedPreferences.getString("cardNumber", "")
        val savedCVV = sharedPreferences.getString("cvv", "")
        val savedExpiryDate = sharedPreferences.getString("expiryDate", "")

        // Відображення балансу
        walletBalanceTextView.text = getString(R.string.wallet_balance, balance)

        // Якщо картка вже була введена, відобразимо її
        if (!savedCardNumber.isNullOrEmpty()) {
            cardNumberEditText.setText(savedCardNumber)
        }
        if (!savedCVV.isNullOrEmpty()) {
            cvvEditText.setText(savedCVV)
        }
        if (!savedExpiryDate.isNullOrEmpty()) {
            expiryDateEditText.setText(savedExpiryDate)
        }

        saveCardButton.setOnClickListener {
            val cardNumber = cardNumberEditText.text.toString().trim()
            val cvv = cvvEditText.text.toString().trim()
            val expiryDate = expiryDateEditText.text.toString().trim()

            if (cardNumber.isEmpty() || cvv.isEmpty() || expiryDate.isEmpty()) {
                Toast.makeText(this, "Fill all fields!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Збереження картки у SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putString("cardNumber", cardNumber)
            editor.putString("cvv", cvv)
            editor.putString("expiryDate", expiryDate)
            editor.apply()

            Toast.makeText(this, "Card saved successfully!", Toast.LENGTH_SHORT).show()
        }

        // Кнопка "Назад"
        backButton.setOnClickListener {
            finish()
        }
    }
}
