package com.example.recipesapp.data

import com.example.recipesapp.models.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object UserRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val userCollection = firestore.collection("users")

    // Agregar un usuario a Firestore
    suspend fun addUser(username: String, email: String, password: String) {
        val user = User(username, email, password)
        val data = hashMapOf(
            "username" to user.username,
            "email" to user.email,
            "password" to user.password
        )
        userCollection.add(data).await() // Agregamos el usuario a Firestore
    }

    // Validar usuario (email y password) en Firestore
    suspend fun validateUser(email: String, password: String): Boolean {
        return try {
            val snapshot = userCollection.whereEqualTo("email", email).whereEqualTo("password", password).get().await()
            snapshot.documents.isNotEmpty() // Si hay un documento, significa que el usuario es válido
        } catch (e: Exception) {
            false // Si hay un error, devolvemos false
        }
    }

    // Obtener todos los usuarios de Firestore
    suspend fun getAllUsers(): List<User> {
        return try {
            val snapshot = userCollection.get().await()
            snapshot.documents.map { document ->
                val username = document.getString("username") ?: ""
                val email = document.getString("email") ?: ""
                val password = document.getString("password") ?: ""
                User(username, email, password)
            }
        } catch (e: Exception) {
            emptyList() // Si hay un error, devolvemos una lista vacía
        }
    }

    // Actualizar un usuario en Firestore
    suspend fun updateUser(email: String, username: String, newPassword: String) {
        val userSnapshot = userCollection.whereEqualTo("email", email).get().await()

        if (!userSnapshot.isEmpty) {
            val userDoc = userSnapshot.documents[0].reference
            val updates = hashMapOf(
                "username" to username
            )
            if (newPassword.isNotEmpty()) {
                updates["password"] = newPassword
            }
            userDoc.update(updates as Map<String, Any>).await()
        }
    }

    // Obtener un usuario por su email
    suspend fun getUserByEmail(email: String): User? {
        return try {
            val snapshot = userCollection.whereEqualTo("email", email).get().await()
            if (snapshot.documents.isNotEmpty()) {
                val document = snapshot.documents[0]
                val username = document.getString("username") ?: ""
                val email = document.getString("email") ?: ""
                val password = document.getString("password") ?: ""
                User(username, email, password) // Devuelve el usuario encontrado
            } else {
                null // Si no se encuentra, devuelve null
            }
        } catch (e: Exception) {
            null // Si hay un error, devuelve null
        }
    }


}
