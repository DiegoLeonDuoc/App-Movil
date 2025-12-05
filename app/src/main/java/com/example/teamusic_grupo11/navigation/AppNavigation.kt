package com.example.teamusic_grupo11.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.teamusic_grupo11.ui.screens.ProfileEditScreen
import com.example.teamusic_grupo11.ui.screens.BibliotecaScreen
import com.example.teamusic_grupo11.ui.screens.ExplorarScreen
import com.example.teamusic_grupo11.ui.screens.HomeScreen
import com.example.teamusic_grupo11.ui.screens.ProfileScreen
import com.example.teamusic_grupo11.ui.screens.RegistroScreen
import com.example.teamusic_grupo11.ui.screens.ResumenScreen
import com.example.teamusic_grupo11.ui.screens.SettingsScreen
import com.example.teamusic_grupo11.ui.screens.AppearanceScreen
import com.example.teamusic_grupo11.ui.screens.FullPlayerScreen
import com.example.teamusic_grupo11.ui.screens.MainScreen
import com.example.teamusic_grupo11.viewmodel.MainViewModel
import com.example.teamusic_grupo11.viewmodel.PlayerViewModel
import com.example.teamusic_grupo11.viewmodel.UsuarioViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AppNavigation(mainViewModel: MainViewModel) {
    val navController = rememberNavController()
    val usuarioViewModel: UsuarioViewModel = viewModel()
    val playerViewModel: PlayerViewModel = viewModel()
    // drawerState and scope are now managed in MainScreen

    LaunchedEffect(key1 = Unit) {
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

    MainScreen(
        navController = navController,
        viewModel = mainViewModel,
        playerViewModel = playerViewModel
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route
        ) {
            composable(route = Screen.Home.route) {
                HomeScreen(
                    navController = navController,
                    viewModel = mainViewModel,
                    playerViewModel = playerViewModel
                )
            }
            composable(route = Screen.Profile.route) {
                ProfileScreen(
                    navController = navController,
                    viewModel = mainViewModel
                )
            }
            composable(route = Screen.Settings.route) {
                SettingsScreen(
                    navController = navController,
                    viewModel = mainViewModel
                )
            }
            composable(route = Screen.Appearance.route) {
                AppearanceScreen(
                    navController = navController,
                    viewModel = mainViewModel
                )
            }
            composable(route = Screen.Resumen.route) {
                ResumenScreen(usuarioViewModel)
            }
            composable(Screen.Registro.route) {
                RegistroScreen(
                    navController = navController,
                    userViewModel = usuarioViewModel,
                    viewModel = mainViewModel
                )
            }
            composable(Screen.Explore.route) {
                ExplorarScreen(
                    navController = navController,
                    viewModel = mainViewModel
                )
            }
            composable(Screen.Library.route) {
                BibliotecaScreen(
                    navController = navController,
                    viewModel = mainViewModel,
                    playerViewModel = playerViewModel  // ¡Este es el parámetro que faltaba!
                )
            }

            composable(route = Screen.ProfileEdit.route) {
                val uiState by mainViewModel.uiState.collectAsState()
                ProfileEditScreen(
                    navController = navController,
                    viewModel = mainViewModel,
                    uiState = uiState
                )
            }

            composable(route = Screen.Player.route) {
                FullPlayerScreen(
                    navController = navController,
                    playerViewModel = playerViewModel
                )
            }
        }
    }
}