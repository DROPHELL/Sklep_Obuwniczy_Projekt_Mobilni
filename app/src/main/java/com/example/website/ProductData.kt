package com.example.website

import android.content.Context

object ProductData {
    private val allProducts = listOf(
        Product("Jordan Spizike", 169.0, 129.0, R.drawable.spizike1),
        Product("White Thunder", 179.0, 139.0, R.drawable.white_thunder1),
        Product("Adidas Samba", 149.0, 109.0, R.drawable.samba1),
        Product("Adidas Spezial", 159.0, 119.0, R.drawable.spezial1),
        Product("Nike Run", 129.0, 99.0, R.drawable.run1),
        Product("New Balance 550", 189.0, 139.0, R.drawable.new_balan1),
        Product("New Balance Belek", 169.0, 119.0, R.drawable.new_bele1),
        Product("Retro Max Air", 209.0, 149.0, R.drawable.rma1),
        Product("Jordan Low SE", 179.0, 129.0, R.drawable.low_se1),
        Product("Samba OG", 149.0, 109.0, R.drawable.sambaog1)
    )

    private var favorites = mutableSetOf<Product>()
    private var cart = mutableSetOf<Product>()
    private var sessionManager: SessionManager? = null

    fun init(context: Context, userId: String) {
        sessionManager = SessionManager(context, userId)
        favorites = sessionManager!!.getFavorites().toMutableSet()
        cart = sessionManager!!.getCart().toMutableSet()
    }

    fun getAll(): List<Product> = allProducts

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

    fun clearAll() {
        favorites.clear()
        cart.clear()
        sessionManager?.clearAll()
    }
}
