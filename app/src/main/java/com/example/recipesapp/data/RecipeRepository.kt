package com.example.recipesapp.data

import com.example.recipesapp.models.Recipe

object RecipeRepository {
    private val recipes = mutableListOf(
        Recipe(1, "Bruschetta", "A simple Italian appetizer", 1, "https://example.com/images/bruschetta.png"),
        Recipe(2, "Caesar Salad", "A classic salad with a tangy dressing", 1, "https://example.com/images/caesar_salad.png"),
        Recipe(3, "Spaghetti Carbonara", "A rich and creamy pasta dish", 2, "https://example.com/images/spaghetti_carbonara.png"),
        Recipe(4, "Grilled Chicken", "Juicy grilled chicken with herbs", 2, "https://example.com/images/grilled_chicken.png"),
        Recipe(5, "Chocolate Cake", "Decadent and moist chocolate cake", 3, "https://example.com/images/chocolate_cake.png"),
        Recipe(6, "Tiramisu", "An Italian dessert with layers of cream and coffee", 3, "https://example.com/images/tiramisu.png"),
        Recipe(7, "Lemonade", "Refreshing homemade lemonade", 4, "https://example.com/images/lemonade.png"),
        Recipe(8, "Margarita", "A classic cocktail with tequila and lime", 4, "https://example.com/images/margarita.png")
    )

    fun getAllRecipes(): List<Recipe> {
        return recipes
    }

    fun getRecipesByCategory(categoryId: Int): List<Recipe> {
        return recipes.filter { it.categoryId == categoryId }
    }

    fun addRatingToRecipe(recipeId: Int, rating: Int) {
        val recipe = recipes.find { it.id == recipeId }
        recipe?.ratings?.add(rating)
    }

    fun getAverageRatingForRecipe(recipeId: Int): Double {
        val recipe = recipes.find { it.id == recipeId }
        return recipe?.getAverageRating() ?: 0.0
    }
}
