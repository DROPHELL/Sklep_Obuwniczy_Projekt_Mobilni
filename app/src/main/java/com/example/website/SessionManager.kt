package com.example.website

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SessionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
    private val gson = Gson()

    // 🔹 Favorites
    fun saveFavorites(favorites: List<Product>) {
        prefs.edit().putString("favorites", gson.toJson(favorites)).apply()
    }

    fun getFavorites(): List<Product> {
        val json = prefs.getString("favorites", null) ?: return emptyList()
        val type = object : TypeToken<List<Product>>() {}.type
        return gson.fromJson(json, type)
    }

    // 🔹 Cart
    fun saveCart(cart: List<Product>) {
        prefs.edit().putString("cart", gson.toJson(cart)).apply()
    }

    fun getCart(): List<Product> {
        val json = prefs.getString("cart", null) ?: return emptyList()
        val type = object : TypeToken<List<Product>>() {}.type
        return gson.fromJson(json, type)
    }

    // 🔹 Shoe Size
    fun saveShoeSize(size: Int) {
        prefs.edit().putInt("shoe_size", size).apply()
    }

    fun getShoeSize(): Int? {
        val size = prefs.getInt("shoe_size", -1)
        return if (size != -1) size else null
    }

    // 🔹 Addresses (simple)
    fun saveAddresses(addresses: List<String>) {
        prefs.edit().putString("addresses", gson.toJson(addresses)).apply()
    }

    fun getAddresses(): List<String> {
        val json = prefs.getString("addresses", null) ?: return emptyList()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type)
    }

    // 🔹 Cards (simple)
    fun saveCards(cards: List<String>) {
        prefs.edit().putString("cards", gson.toJson(cards)).apply()
    }

    fun getCards(): List<String> {
        val json = prefs.getString("cards", null) ?: return emptyList()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type)
    }

    // 🔹 Full Address
    fun saveFullAddress(address: FullAddress) {
        prefs.edit().putString("full_address", gson.toJson(address)).apply()
    }

    fun getFullAddress(): FullAddress? {
        val json = prefs.getString("full_address", null) ?: return null
        return gson.fromJson(json, FullAddress::class.java)
    }

    // 🔹 Full Card
    fun saveFullCard(card: FullCard) {
        prefs.edit().putString("full_card", gson.toJson(card)).apply()
    }

    fun getFullCard(): FullCard? {
        val json = prefs.getString("full_card", null) ?: return null
        return gson.fromJson(json, FullCard::class.java)
    }

    // 🔹 Clear all user data
    fun clearAll() {
        prefs.edit().clear().apply()
    }
}
