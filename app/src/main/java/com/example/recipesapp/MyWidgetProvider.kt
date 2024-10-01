package com.example.recipesapp

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.recipesapp.data.RecipeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.widget_layout)

        CoroutineScope(Dispatchers.IO).launch {
            val featuredRecipe = RecipeRepository.getAllRecipes().maxByOrNull { it.getAverageRating() }

            featuredRecipe?.let { recipe ->
                // Actualizar nombre de la receta
                views.setTextViewText(R.id.widget_recipe_name, recipe.name)

                // Cargar la imagen de la receta usando Coil
                try {
                    val imageLoader = ImageLoader(context)
                    val request = ImageRequest.Builder(context)
                        .data(recipe.imageUrl)
                        .build()

                    val result = (imageLoader.execute(request) as SuccessResult).drawable
                    withContext(Dispatchers.Main) {
                        views.setImageViewBitmap(R.id.widget_image, result.toBitmap())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            // Crear un Intent para abrir la MainActivity con la instrucción de navegar a HomeView
            val intent = Intent(context, MainActivity::class.java).apply {
                putExtra("navigate_to", "home")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Establecemos el clic en todo el widget para abrir la HomeView
            views.setOnClickPendingIntent(R.id.widget_image, pendingIntent)

            // Actualizar el widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }




    override fun onEnabled(context: Context) {
    }

    override fun onDisabled(context: Context) {
    }
}
