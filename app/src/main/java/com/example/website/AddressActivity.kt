package com.example.website

import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AddressActivity : AppCompatActivity() {

    private lateinit var streetEditText: EditText
    private lateinit var cityEditText: EditText
    private lateinit var stateEditText: EditText
    private lateinit var zipCodeEditText: EditText
    private lateinit var countryEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var savedAddressesTextView: TextView
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)


        streetEditText = findViewById(R.id.streetEditText)
        cityEditText = findViewById(R.id.cityEditText)
        stateEditText = findViewById(R.id.stateEditText)
        zipCodeEditText = findViewById(R.id.zipCodeEditText)
        countryEditText = findViewById(R.id.countryEditText)
        saveButton = findViewById(R.id.saveAddressButton)
        savedAddressesTextView = findViewById(R.id.savedAddresses)
        backButton = findViewById(R.id.backButton)

        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)


        val savedAddress = sharedPreferences.getString("deliveryAddress", null)
        savedAddressesTextView.text = savedAddress ?: "No saved addresses."


        saveButton.setOnClickListener {
            val street = streetEditText.text.toString().trim()
            val city = cityEditText.text.toString().trim()
            val state = stateEditText.text.toString().trim()
            val zip = zipCodeEditText.text.toString().trim()
            val country = countryEditText.text.toString().trim()

            if (street.isEmpty() || city.isEmpty() || state.isEmpty() || zip.isEmpty() || country.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val fullAddress = "$street, $city, $state, $zip, $country"
            sharedPreferences.edit().putString("deliveryAddress", fullAddress).apply()

            savedAddressesTextView.text = fullAddress
            Toast.makeText(this, "Address saved", Toast.LENGTH_SHORT).show()
        }


        backButton.setOnClickListener {
            finish()
        }
    }
}
