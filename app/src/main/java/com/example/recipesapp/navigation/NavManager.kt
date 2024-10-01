package com.example.navegacion.navigation

import SessionManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.recipesapp.views.EditProfileView
import com.example.recipesapp.views.HomeView
import com.example.recipesapp.views.LoginView
import com.example.recipesapp.views.PasswordRecoveryView
import com.example.recipesapp.views.RegisterView

@Composable
fun NavManager(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Login"  ){

        composable("signIn"){
            RegisterView(navController)
        }

        composable("login"){
            val context = LocalContext.current
            LoginView(navController, context)
        }

        composable("passwordRecovery"){
            PasswordRecoveryView(navController)
        }

        composable("home"){
            HomeView(navController)
        }

        // Ruta para la vista de edición de perfil
        composable("editProfile") {
            val context = LocalContext.current
            val (email, username) = SessionManager.getUserSession(context)

            // Pasar el email y username a la vista de edición
            EditProfileView(navController = navController, usernameFromSession = username ?: "", emailFromSession = email ?: "")
        }

    }
}