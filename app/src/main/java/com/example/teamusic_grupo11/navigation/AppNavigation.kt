package com.example.teamusic_grupo11.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
// --- 1. IMPORTA LO NUEVO ---
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.teamusic_grupo11.ui.screens.ProfileEditScreen
// --- (fin imports) ---
import com.example.teamusic_grupo11.ui.screens.BibliotecaScreen
import com.example.teamusic_grupo11.ui.screens.ExplorarScreen
import com.example.teamusic_grupo11.ui.screens.HomeScreen
import com.example.teamusic_grupo11.ui.screens.ProfileScreen
import com.example.teamusic_grupo11.ui.screens.RegistroScreen
import com.example.teamusic_grupo11.ui.screens.ResumenScreen
import com.example.teamusic_grupo11.ui.screens.SettingsScreen
import com.example.teamusic_grupo11.ui.screens.AppearanceScreen
import com.example.teamusic_grupo11.viewmodel.MainViewModel
import com.example.teamusic_grupo11.viewmodel.UsuarioViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AppNavigation(mainViewModel: MainViewModel) { // <-- Este es el ViewModel BUENO
    // val viewModel: MainViewModel = viewModel() // <-- 2. BORRA ESTA LÍNEA (Este era el ViewModel MALO)
    val navController = rememberNavController()
    val usuarioViewModel: UsuarioViewModel = viewModel() // Este está bien, es otro ViewModel
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        // 3. Usa el ViewModel BUENO
        mainViewModel.navigationEvents.collectLatest { event ->
            when (event) {
                is NavigationEvent.NavigateTo -> {
                    navController.navigate(event.route.route) {
                        event.popUpToRoute?.let {
                            popUpTo(it.route) {
                                inclusive = event.inclusive
                            }
                            launchSingleTop = event.singleTop
                            restoreState = true
                        }
                    }
                }

                is NavigationEvent.PopBackStack -> navController.popBackStack()
                is NavigationEvent.NavigateUp -> navController.navigateUp()
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // 4. Asegúrate de que TODAS las pantallas usen 'mainViewModel'
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController, viewModel = mainViewModel, drawerState, scope)
        }
        composable(route = Screen.Profile.route) {
            ProfileScreen(navController = navController, viewModel = mainViewModel)
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(navController = navController, viewModel = mainViewModel)
        }
        composable(route = Screen.Appearance.route) {
            AppearanceScreen(navController = navController, viewModel = mainViewModel)
        }
        composable(route=Screen.Resumen.route) {
            ResumenScreen(usuarioViewModel)
        }
        composable(Screen.Registro.route) {
            RegistroScreen(navController,usuarioViewModel, mainViewModel, drawerState, scope)
        }
        composable(Screen.Explore.route) {
            ExplorarScreen(navController, viewModel = mainViewModel, drawerState, scope)
        }
        composable(Screen.Library.route) {
            BibliotecaScreen(navController, mainViewModel, drawerState, scope)
        }

        // --- 5. AÑADE ESTE BLOQUE AL FINAL ---
        composable(route = Screen.ProfileEdit.route) {
            // Recoge el estado del viewModel
            val uiState by mainViewModel.uiState.collectAsState()

            // Muestra la nueva pantalla
            ProfileEditScreen(
                navController = navController,
                viewModel = mainViewModel, // Pasa el viewModel bueno
                uiState = uiState
            )
        }
    }
}