package com.example.website

import android.content.Context

object ProductData {
    private val allProducts = listOf(
        Product("Jordan Spizike", 169.0, 129.0, "spizike"),
        Product("White Thunder", 179.0, 139.0, "white_thunder"),
        Product("Adidas Samba", 149.0, 109.0, "samba"),
        Product("Adidas Spezial", 159.0, 119.0, "spezial"),
        Product("Nike Run", 129.0, 99.0, "run"),
        Product("New Balance 550", 189.0, 139.0, "new_balan"),
        Product("New Balance Belek", 169.0, 119.0, "new_bele"),
        Product("Retro Max Air", 209.0, 149.0, "rma"),
        Product("Jordan Low SE", 179.0, 129.0, "low_se"),
        Product("Samba OG", 149.0, 109.0, "sambaog")
    )

    private var favorites = mutableSetOf<Product>()
    private var cart = mutableSetOf<Product>()
    private var sessionManager: SessionManager? = null

    fun init(context: Context) {
        sessionManager = SessionManager(context)
        favorites = sessionManager!!.getFavorites().toMutableSet()
        cart = sessionManager!!.getCart().toMutableSet()
    }

    fun getAll(): List<Product> = allProducts

    // Favorites
    fun addToFavorites(product: Product) {
        favorites.add(product)
        sessionManager?.saveFavorites(favorites.toList())
    }

    fun removeFromFavorites(product: Product) {
        favorites.remove(product)
        sessionManager?.saveFavorites(favorites.toList())
    }

    fun isFavorite(product: Product): Boolean = favorites.contains(product)
    fun getFavorites(): List<Product> = favorites.toList()

    // Cart
    fun addToCart(product: Product) {
        cart.add(product)
        sessionManager?.saveCart(cart.toList())
    }

    fun removeFromCart(product: Product) {
        cart.remove(product)
        sessionManager?.saveCart(cart.toList())
    }

    fun isInCart(product: Product): Boolean = cart.contains(product)
    fun getCart(): List<Product> = cart.toList()

    fun clearCart() {
        cart.clear()
        sessionManager?.saveCart(emptyList())
    }


    fun clearAll() {
        favorites.clear()
        cart.clear()
        sessionManager?.clearAll()
    }
}
