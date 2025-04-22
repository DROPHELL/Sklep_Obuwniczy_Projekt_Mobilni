package com.example.website

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SessionManager(context: Context, private val userId: String) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_data_$userId", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveFavorites(favorites: List<Product>) {
        val json = gson.toJson(favorites)
        prefs.edit().putString("favorites", json).apply()
    }

    fun getFavorites(): List<Product> {
        val json = prefs.getString("favorites", null) ?: return emptyList()
        val type = object : TypeToken<List<Product>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveCart(cart: List<Product>) {
        val json = gson.toJson(cart)
        prefs.edit().putString("cart", json).apply()
    }

    fun getCart(): List<Product> {
        val json = prefs.getString("cart", null) ?: return emptyList()
        val type = object : TypeToken<List<Product>>() {}.type
        return gson.fromJson(json, type)
    }

    fun clearAll() {
        prefs.edit().clear().apply()
    }
}
