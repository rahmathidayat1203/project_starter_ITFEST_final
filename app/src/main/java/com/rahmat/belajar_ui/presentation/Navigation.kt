package com.rahmat.belajar_ui.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rahmat.belajar_ui.ui.home.HomeScreen
import com.rahmat.belajar_ui.ui.login.LoginScreen
import com.rahmat.belajar_ui.ui.registration.RegisterScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onNavigateToRegister = { navController.navigate("register") },
                onLoginSuccess = { navController.navigate("home") }
            )
        }

        composable("register") {
            RegisterScreen(
                onRegistrationSuccess = { navController.navigate("login") }
            )
        }
        composable("home") {
            HomeScreen(
                onLogout = { navController.navigate("login") { popUpTo("home") { inclusive = true } } }
            )
        }
    }
}
