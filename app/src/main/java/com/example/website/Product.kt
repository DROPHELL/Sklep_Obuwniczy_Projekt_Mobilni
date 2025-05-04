package com.example.website

import java.io.Serializable

data class Product(
    val name: String,
    val oldPrice: Double,
    val newPrice: Double,
    val imageNameBase: String,
    var selectedSize: Int? = null
) : Serializable
