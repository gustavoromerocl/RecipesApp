package com.example.recipesapp.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.recipesapp.data.CategoryRepository
import com.example.recipesapp.data.RecipeRepository
import com.example.recipesapp.models.Category
import com.example.recipesapp.models.Recipe

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(navController: NavController) {
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var categories by remember { mutableStateOf<List<Category>>(emptyList()) } // State para las categorías
    var featuredRecipes by remember { mutableStateOf<List<Recipe>>(emptyList()) } // State para las recetas destacadas
    var displayedRecipes by remember { mutableStateOf<List<Recipe>>(emptyList()) } // State para las recetas mostradas

    // Usamos LaunchedEffect para cargar categorías y recetas
    LaunchedEffect(Unit) {
        categories = CategoryRepository.getAllCategories()
        featuredRecipes = RecipeRepository.getAllRecipes().sortedByDescending { it.getAverageRating() }
        displayedRecipes = featuredRecipes // Inicialmente mostrar todas las recetas ordenadas por calificación
    }

    // Si cambia la categoría seleccionada, cargar las recetas correspondientes
    LaunchedEffect(selectedCategory) {
        if (selectedCategory == null) {
            displayedRecipes = featuredRecipes // Mostrar recetas destacadas si no hay categoría seleccionada
        } else {
            displayedRecipes = RecipeRepository.getRecipesByCategory(selectedCategory!!.id)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Home") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // LazyRow para mostrar las categorías
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        // Agregar la categoría destacada (todas las recetas)
                        CategoryItem(
                            category = Category(0, "Destacado", Icons.Filled.Star),
                            isSelected = selectedCategory?.id == 0,
                            onClick = {
                                selectedCategory = null // Establecer a null para mostrar recetas destacadas
                            }
                        )
                    }

                    // Mostrar las categorías disponibles cuando se carguen
                    items(categories.size) { index ->
                        val category = categories[index]
                        CategoryItem(
                            category = category,
                            isSelected = selectedCategory?.id == category.id,
                            onClick = {
                                selectedCategory = category // Actualizar la categoría seleccionada
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // LazyColumn para mostrar las recetas
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(displayedRecipes.size) { index ->
                        val recipe = displayedRecipes[index]
                        RecipeItem(recipe = recipe)
                    }
                }
            }
        }
    )
}

@Composable
fun CategoryItem(category: Category, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(4.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = category.name,
                tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = category.name,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}


@Composable
fun RecipeItem(recipe: Recipe) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen de la receta a la izquierda usando AsyncImage
            AsyncImage(
                model = recipe.imageUrl,
                contentDescription = recipe.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = recipe.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = recipe.description,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // Calificación de la receta en la esquina superior derecha
            Text(
                text = String.format("%.1f", recipe.getAverageRating()),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.Top)
                    .padding(8.dp)
            )
        }
    }
}

