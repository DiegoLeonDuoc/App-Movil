package com.example.appnombre_grupox.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appnombre_grupox.ui.screens.HomeScreen
import com.example.appnombre_grupox.ui.screens.HomeScreenCompacta
import com.example.appnombre_grupox.ui.screens.HomeScreenMediana
import com.example.appnombre_grupox.ui.screens.ProfileScreen
import com.example.appnombre_grupox.ui.screens.RegistroScreen
import com.example.appnombre_grupox.ui.screens.ResumenScreen
import com.example.appnombre_grupox.ui.screens.SettingsScreen
import com.example.appnombre_grupox.ui.utils.obtenerWindowSizeClass
import com.example.appnombre_grupox.viewmodel.MainViewModel
import com.example.appnombre_grupox.viewmodel.UsuarioViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AppNavigation() {
    val viewModel: MainViewModel = viewModel()
    val navController = rememberNavController()
    val usuarioViewModel: UsuarioViewModel = viewModel()
    val innerPadding = 16.dp

    LaunchedEffect(key1 = Unit) {
        viewModel.navigationEvents.collectLatest { event ->
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
        startDestination = Screen.Home.route,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(route = Screen.Home.route) {
//            val windowsSizeClass = obtenerWindowSizeClass()
//            when (windowsSizeClass.widthSizeClass) {
//                WindowWidthSizeClass.Compact -> HomeScreenCompacta(navController = navController, viewModel = viewModel)
//                WindowWidthSizeClass.Medium -> HomeScreenMediana(navController = navController, viewModel = viewModel)
//                WindowWidthSizeClass.Expanded -> HomeScreen(navController = navController, viewModel = viewModel)
//            }
            HomeScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = Screen.Profile.route) {
            ProfileScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(navController = navController, viewModel = viewModel)
        }
        composable(route=Screen.Resumen.route) {
            ResumenScreen(usuarioViewModel)
        }
        composable(Screen.Registro.route) {
            RegistroScreen(navController,usuarioViewModel)
        }
    }
}