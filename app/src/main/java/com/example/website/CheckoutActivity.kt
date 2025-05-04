package com.example.website

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class CheckoutActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var streetEditText: EditText
    private lateinit var cityEditText: EditText
    private lateinit var stateEditText: EditText
    private lateinit var zipEditText: EditText
    private lateinit var countryEditText: EditText
    private lateinit var cardNumberEditText: EditText
    private lateinit var cvvEditText: EditText
    private lateinit var expiryEditText: EditText
    private lateinit var confirmButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        backButton = findViewById(R.id.backButton)
        streetEditText = findViewById(R.id.streetEditText)
        cityEditText = findViewById(R.id.cityEditText)
        stateEditText = findViewById(R.id.stateEditText)
        zipEditText = findViewById(R.id.zipEditText)
        countryEditText = findViewById(R.id.countryEditText)
        cardNumberEditText = findViewById(R.id.cardNumberEditText)
        cvvEditText = findViewById(R.id.cvvEditText)
        expiryEditText = findViewById(R.id.expiryEditText)
        confirmButton = findViewById(R.id.confirmButton)

        val session = SessionManager(this)


        session.getFullAddress()?.let { address ->
            streetEditText.setText(address.street)
            cityEditText.setText(address.city)
            stateEditText.setText(address.state)
            zipEditText.setText(address.zip)
            countryEditText.setText(address.country)
        }

        session.getFullCard()?.let { card ->

            expiryEditText.setText(card.expiry)
        }


        confirmButton.setOnClickListener {
            val street = streetEditText.text.toString().trim()
            val city = cityEditText.text.toString().trim()
            val state = stateEditText.text.toString().trim()
            val zip = zipEditText.text.toString().trim()
            val country = countryEditText.text.toString().trim()
            val cardNumber = cardNumberEditText.text.toString().trim()
            val cvv = cvvEditText.text.toString().trim()
            val expiry = expiryEditText.text.toString().trim()

            if (street.isEmpty() || city.isEmpty() || state.isEmpty() || zip.isEmpty()
                || country.isEmpty() || cardNumber.isEmpty() || cvv.isEmpty() || expiry.isEmpty()
            ) {
                Toast.makeText(this, "Enter all data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val address = FullAddress(street, city, country, zip, state)
            val card = FullCard(cardNumber, cvv, expiry)

            session.saveFullAddress(address)
            session.saveFullCard(card)


            ProductData.clearCart()

            Toast.makeText(this, "Order placed!", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }


        backButton.setOnClickListener {
            finish()
        }
    }
}
