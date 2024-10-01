package com.example.recipesapp.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.example.recipesapp.models.Category
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object CategoryRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val categoryCollection = firestore.collection("categories")

    suspend fun getAllCategories(): List<Category> {
        return try {
            val snapshot = categoryCollection.get().await()
            snapshot.documents.map { document: com.google.firebase.firestore.DocumentSnapshot ->  // Especificar el tipo aquí
                val id = document.getLong("id")?.toInt() ?: 0
                val name = document.getString("name") ?: ""
                val iconName = document.getString("iconName") ?: "Fastfood"

                // Usamos un icono de Material Icons basado en el nombre almacenado
                val icon = when (iconName) {
                    "Fastfood" -> Icons.Filled.Fastfood
                    "Restaurant" -> Icons.Filled.Restaurant
                    "Cake" -> Icons.Filled.Cake
                    "LocalBar" -> Icons.Filled.LocalBar
                    else -> Icons.Filled.Fastfood
                }

                Category(id, name, icon)
            }
        } catch (e: Exception) {
            emptyList() // Si ocurre un error, retornamos una lista vacía
        }
    }


    suspend fun getCategoryById(id: Int): Category? {
        return try {
            val snapshot = categoryCollection.whereEqualTo("id", id).get().await()
            val document = snapshot.documents.firstOrNull()
            document?.let {
                val name = it.getString("name") ?: ""
                val iconName = it.getString("iconName") ?: "Fastfood"

                val icon = when (iconName) {
                    "Fastfood" -> Icons.Filled.Fastfood
                    "Restaurant" -> Icons.Filled.Restaurant
                    "Cake" -> Icons.Filled.Cake
                    "LocalBar" -> Icons.Filled.LocalBar
                    else -> Icons.Filled.Fastfood
                }

                Category(id, name, icon)
            }
        } catch (e: Exception) {
            null // Si ocurre un error, retornamos null
        }
    }

    // Si necesitas agregar una categoría a Firestore
    suspend fun addCategory(category: Category) {
        val data = hashMapOf(
            "id" to category.id,
            "name" to category.name,
            "iconName" to when (category.icon) {
                Icons.Filled.Fastfood -> "Fastfood"
                Icons.Filled.Restaurant -> "Restaurant"
                Icons.Filled.Cake -> "Cake"
                Icons.Filled.LocalBar -> "LocalBar"
                else -> "Fastfood"
            }
        )
        categoryCollection.add(data).await()
    }

    //Poblar data de prueba
    suspend fun insertDefaultCategories() {
        val snapshot = categoryCollection.get().await()
        if (snapshot.isEmpty) {
            val defaultCategories = listOf(
                Category(1, "Aperitivos", Icons.Filled.Fastfood),
                Category(2, "Platos Principales", Icons.Filled.Restaurant),
                Category(3, "Postres", Icons.Filled.Cake),
                Category(4, "Bebestibles", Icons.Filled.LocalBar)
            )

            defaultCategories.forEach { category ->
                addCategory(category) // Utilizamos la función addCategory para agregar la categoría
            }
        }
    }
}
