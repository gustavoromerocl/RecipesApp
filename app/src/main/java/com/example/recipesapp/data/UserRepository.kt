package com.example.recipesapp.data

import com.example.recipesapp.models.User

object UserRepository {
    private val users = mutableListOf(
        User("user1", "user1@example.com", "password1"),
        User("user2", "user2@example.com", "password2"),
        User("user3", "user3@example.com", "password3"),
        User("user4", "user4@example.com", "password4"),
        User("user5", "user5@example.com", "password5")
    )

    fun addUser(username: String, email: String, password: String) {
        users.add(User(username, email, password))
    }

    fun validateUser(email: String, password: String): Boolean {
        return users.any { it.email == email && it.password == password }
    }

    fun getAllUsers(): List<User> {
        return users
    }
}
