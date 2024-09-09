package com.example.recipesapp.data

import com.example.recipesapp.models.Recipe

object RecipeRepository {
    private val recipes = mutableListOf(
        Recipe(
            id = 1,
            name = "Bruschetta",
            description = "Un simple aperitivo italiano",
            categoryId = 1,
            imageUrl = "https://bing.com/th?id=OSK.ab0bca62267032c576825d6fa814939c",
            ratings = mutableListOf(4, 5, 5, 4)
        ),
        Recipe(
            id = 2,
            name = "Ensalada César",
            description = "Una ensalada clásica con un aderezo picante",
            categoryId = 1,
            imageUrl = "https://th.bing.com/th/id/OIP.ACHJGA4bBbw0-NKxsWd_mgHaE8?rs=1&pid=ImgDetMain",
            ratings = mutableListOf(5, 4, 5, 3)
        ),
        Recipe(
            id = 3,
            name = "Espaguetis a la carbonara",
            description = "Un plato de pasta rico y cremoso",
            categoryId = 2,
            imageUrl = "https://www.supermomix.com/wp-content/uploads/2018/03/diary-free-carbonara.jpg",
            ratings = mutableListOf(5, 5, 5)
        ),
        Recipe(
            id = 4,
            name = "Pollo a la parrilla",
            description = "Jugoso pollo a la parrilla con hierbas",
            categoryId = 2,
            imageUrl = "https://th.bing.com/th/id/OIP.KHigMAViT5myvxE8Zo43OgHaE8?rs=1&pid=ImgDetMain",
            ratings = mutableListOf(3, 4, 4)
        ),
        Recipe(
            id = 5,
            name = "Tarta de chocolate",
            description = "Tarta de chocolate decadente y húmeda",
            categoryId = 3,
            imageUrl = "https://th.bing.com/th/id/R.0771a8c605d3e1e505f7e894390dfbe1?rik=C9DacoHcoPQG0w&riu=http%3a%2f%2fcdn2.cocinadelirante.com%2fsites%2fdefault%2ffiles%2fimages%2f2017%2f10%2fpasteldechocolateperfecto.jpg&ehk=zEbIDb4VdLxvG0Z0JeYVEB6kaAOpwpFaiJEue8E20Bw%3d&risl=&pid=ImgRaw&r=0",
            ratings = mutableListOf(5, 5, 5, 5)
        ),
        Recipe(
            id = 6,
            name = "Tiramisu",
            description = "Un postre italiano con capas de crema y café",
            categoryId = 3,
            imageUrl = "https://th.bing.com/th/id/R.83a3822f9a9b63e367a7e8d47b7ab379?rik=Gdp4AY0dTp763w&pid=ImgRaw&r=0",
            ratings = mutableListOf(4, 5, 4)
        ),
        Recipe(
            id = 7,
            name = "Limonada",
            description = "Refrescante limonada casera",
            categoryId = 4,
            imageUrl = "https://th.bing.com/th/id/R.3b852bcface642f62c4bc943ae06a025?rik=EKEI16Bi%2b0EWhA&riu=http%3a%2f%2fsalpimenta.com.ar%2fwp-content%2fuploads%2f2018%2f04%2fLimonada-1068x710.jpg&ehk=vTCgbnDXnMVd8T2VQADvK5HLmjDeqgOrba37DXa%2f6nM%3d&risl=&pid=ImgRaw&r=0",
            ratings = mutableListOf(3, 4)
        ),
        Recipe(
            id = 8,
            name = "Margarita",
            description = "Un cóctel clásico con tequila y lima",
            categoryId = 4,
            imageUrl = "https://th.bing.com/th/id/OIP.gdaXvN32MxzA7Nd7slydHQHaEo?rs=1&pid=ImgDetMain",
            ratings = mutableListOf(4, 5, 4)
        )
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
