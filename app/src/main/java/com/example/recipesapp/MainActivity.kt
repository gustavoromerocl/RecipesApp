package com.example.recipesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.recipesapp.data.CategoryRepository
import com.example.navegacion.navigation.NavManager
import com.example.recipesapp.data.RecipeRepository
import com.example.recipesapp.ui.theme.RecipesAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Poblar las entidades por defecto al iniciar la app
        lifecycleScope.launch {
            CategoryRepository.insertDefaultCategories()
            RecipeRepository.insertDefaultRecipes()
        }


        setContent {
            RecipesAppTheme {
                NavManager()
            }
        }
    }
}
