package com.example.recipesapp.data

import com.example.recipesapp.models.Recipe
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

// Interfaz para el repositorio
interface RecipeRepositoryInterface {
    suspend fun getAllRecipes(): List<Recipe>
    suspend fun getRecipesByCategory(categoryId: Int): List<Recipe>
    suspend fun addRatingToRecipe(recipeId: Int, rating: Int)
    suspend fun addRecipe(recipe: Recipe)
    suspend fun getAverageRatingForRecipe(recipeId: Int): Double
}

// Implementación mockeada del repositorio para pruebas
class RecipeRepositoryMock : RecipeRepositoryInterface {

    private val recipes = mutableListOf(
        Recipe(1, "Bruschetta", "Un simple aperitivo italiano", 1, "https://image-url.com", mutableListOf(5, 4)),
        Recipe(2, "Ensalada César", "Una ensalada clásica", 1, "https://image-url.com", mutableListOf(4, 5)),
        Recipe(3, "Tiramisu", "Un postre italiano clásico", 2, "https://image-url.com", mutableListOf(5, 5, 5))
    )

    override suspend fun getAllRecipes(): List<Recipe> {
        return recipes
    }

    override suspend fun getRecipesByCategory(categoryId: Int): List<Recipe> {
        return recipes.filter { it.categoryId == categoryId }
    }

    override suspend fun addRatingToRecipe(recipeId: Int, rating: Int) {
        val recipe = recipes.find { it.id == recipeId }
        recipe?.ratings?.add(rating)
    }

    override suspend fun addRecipe(recipe: Recipe) {
        recipes.add(recipe)
    }

    override suspend fun getAverageRatingForRecipe(recipeId: Int): Double {
        val recipe = recipes.find { it.id == recipeId }
        return recipe?.getAverageRating() ?: 0.0
    }
}

// Clase de prueba para RecipeRepository usando el mock
class RecipeRepositoryTest {

    private lateinit var recipeRepository: RecipeRepositoryMock

    @Before
    fun setUp() {
        // Usamos la implementación mockeada
        recipeRepository = RecipeRepositoryMock()
    }

    @Test
    fun getAllRecipes_returnsNonEmptyList() = runBlocking {
        val recipes = recipeRepository.getAllRecipes()
        assertTrue(recipes.isNotEmpty())  // Verificamos que la lista no esté vacía
    }

    @Test
    fun addRecipe_addsRecipeToList() = runBlocking {
        val newRecipe = Recipe(4, "Nuevo Plato", "Descripción de prueba", 3, "https://image-url.com", mutableListOf())
        recipeRepository.addRecipe(newRecipe)

        val recipes = recipeRepository.getAllRecipes()

        // Verificamos que la nueva receta esté en la lista
        val foundRecipe = recipes.find { it.id == 4 }
        assertNotNull(foundRecipe)
    }

    @Test
    fun getRecipesByCategory_returnsCorrectRecipes() = runBlocking {
        val category1Recipes = recipeRepository.getRecipesByCategory(1)

        // Verificamos que las recetas sean de la categoría correcta
        assertTrue(category1Recipes.all { it.categoryId == 1 })
        assertEquals(2, category1Recipes.size)  // Sabemos que debería haber 2 recetas
    }

    @Test
    fun addRatingToRecipe_addsRatingCorrectly() = runBlocking {
        // Añadimos una nueva calificación a la receta con id 1
        recipeRepository.addRatingToRecipe(1, 5)

        val recipe = recipeRepository.getAllRecipes().find { it.id == 1 }

        // Verificamos que la calificación haya sido añadida
        assertEquals(3, recipe?.ratings?.size)  // Originalmente tenía 2 calificaciones
        assertEquals(5, recipe?.ratings?.last())  // La última calificación añadida es 5
    }

    @Test
    fun getAverageRatingForRecipe_calculatesCorrectAverage() = runBlocking {
        val averageRating = recipeRepository.getAverageRatingForRecipe(1)

        // La receta con id 1 tiene las calificaciones (5, 4)
        assertEquals(4.5, averageRating, 0.0)
    }
}
