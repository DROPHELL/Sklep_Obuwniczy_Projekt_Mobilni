package com.example.website

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast

object CheckoutDialog {
    fun show(
        context: Context,
        addresses: List<String>,
        cards: List<String>,
        onConfirm: (address: String, card: String) -> Unit
    ) {
        if (addresses.isEmpty() || cards.isEmpty()) {
            Toast.makeText(context, "Brak zapisanych adresów lub kart", Toast.LENGTH_SHORT).show()
            return
        }

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_checkout, null)
        val addressSpinner = view.findViewById<Spinner>(R.id.addressSpinner)
        val cardSpinner = view.findViewById<Spinner>(R.id.cardSpinner)

        // Адаптери для списків
        val addressAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, addresses)
        val cardAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, cards)

        addressSpinner.adapter = addressAdapter
        cardSpinner.adapter = cardAdapter

        AlertDialog.Builder(context)
            .setTitle("Potwierdź zamówienie")
            .setView(view)
            .setPositiveButton("Zamawiam") { _, _ ->
                val selectedAddress = addressSpinner.selectedItem.toString()
                val selectedCard = cardSpinner.selectedItem.toString()
                onConfirm(selectedAddress, selectedCard)
            }
            .setNegativeButton("Anuluj", null)
            .show()
    }
}
