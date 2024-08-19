package com.example.navegacion.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
            LoginView(navController)
        }

        composable("passwordRecovery"){
            PasswordRecoveryView(navController)
        }

        composable("home"){
            HomeView(navController)
        }

    }
}