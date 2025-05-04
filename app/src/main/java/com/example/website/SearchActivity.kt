package com.example.website

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class SearchActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var searchResultsRecyclerView: RecyclerView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var backButton: ImageButton
    private lateinit var adapter: ProductAdapter
    private lateinit var resultsTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchView = findViewById(R.id.searchView)
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        backButton = findViewById(R.id.backButton)
        resultsTitle = findViewById(R.id.resultsTitle)


        searchResultsRecyclerView.layoutManager = GridLayoutManager(this, 2)

        adapter = ProductAdapter(emptyList()) { product ->
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("product", product)
            startActivity(intent)
        }
        searchResultsRecyclerView.adapter = adapter

        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).putExtra("tab", "home"))
            finish()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    searchProducts(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    clearResults()
                }
                return true
            }
        })

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    startActivity(Intent(this, MainActivity::class.java).putExtra("tab", "home"))
                    finish()
                    true
                }
                R.id.menu_search -> true
                R.id.menu_favorites -> {
                    startActivity(Intent(this, FavoritesActivity::class.java))
                    finish()
                    true
                }
                R.id.menu_cart -> {
                    startActivity(Intent(this, CartActivity::class.java))
                    finish()
                    true
                }
                R.id.menu_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }

        bottomNavigationView.selectedItemId = R.id.menu_search
    }

    private fun searchProducts(query: String) {
        val allProducts = ProductData.getAll()
        val filtered = allProducts.filter {
            it.name.contains(query, ignoreCase = true)
        }

        adapter = ProductAdapter(filtered) { product ->
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("product", product)
            startActivity(intent)
        }
        searchResultsRecyclerView.adapter = adapter

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchView.windowToken, 0)
        searchView.clearFocus()

        if (filtered.isEmpty()) {
            Toast.makeText(this, "No results", Toast.LENGTH_SHORT).show()
            resultsTitle.visibility = View.GONE
        } else {
            resultsTitle.visibility = View.VISIBLE
        }
    }

    private fun clearResults() {
        resultsTitle.visibility = View.GONE
        adapter = ProductAdapter(emptyList()) { product ->
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("product", product)
            startActivity(intent)
        }
        searchResultsRecyclerView.adapter = adapter
    }
}
