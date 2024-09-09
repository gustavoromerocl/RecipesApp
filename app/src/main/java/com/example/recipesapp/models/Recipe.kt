package com.example.recipesapp.models

data class Recipe(
    val id: Int,
    val name: String,
    val description: String,
    val categoryId: Int,
    val imageUrl: String,
    val ratings: MutableList<Int> = mutableListOf() // Lista de calificaciones
) {
    fun getAverageRating(): Double {
        return if (ratings.isNotEmpty()) {
            ratings.average() // Calcular el promedio de las calificaciones
        } else {
            0.0
        }
    }
}
