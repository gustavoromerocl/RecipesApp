package com.example.recipesapp.data

import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import com.example.recipesapp.models.Category

object CategoryRepository {
    private val categories = mutableListOf(
        Category(1, "Appetizers", Icons.Filled.Fastfood),
        Category(2, "Main Courses", Icons.Filled.Restaurant),
        Category(3, "Desserts", Icons.Filled.Cake),
        Category(4, "Beverages", Icons.Filled.LocalBar)
    )

    fun getAllCategories(): List<Category> {
        return categories
    }

    fun getCategoryById(id: Int): Category? {
        return categories.find { it.id == id }
    }
}
