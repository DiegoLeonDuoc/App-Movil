package com.example.teamusic_grupo11.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.teamusic_grupo11.data.Songs
import com.example.teamusic_grupo11.navigation.Screen
import com.example.teamusic_grupo11.ui.components.BottomBar
import com.example.teamusic_grupo11.ui.components.MiniPlayer
import com.example.teamusic_grupo11.ui.components.TopBar
import com.example.teamusic_grupo11.viewmodel.MainViewModel
import com.example.teamusic_grupo11.viewmodel.PlayerViewModel
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel,
    playerViewModel: PlayerViewModel,
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    // Define which routes should show the TopBar
    val routesWithTopBar = setOf(
        Screen.Home.route,
        Screen.Explore.route,
        Screen.Library.route
    )
    val shouldShowTopBar = currentDestination?.route in routesWithTopBar

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Menú", modifier = Modifier.padding(16.dp))
                NavigationDrawerItem(
                    label = { Text("Perfil") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        viewModel.navigateTo(Screen.Profile)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Registro") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        viewModel.navigateTo(Screen.Registro)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Configuración") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        viewModel.navigateTo(Screen.Settings)
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                if (shouldShowTopBar) {
                    TopBar(viewModel, drawerState, scope)
                }
            },
            bottomBar = {
                Column {
                    // MiniPlayer sits above the BottomBar
                    MiniPlayer(
                        playerViewModel = playerViewModel,
                        onExpandClick = {
                            navController.navigate(Screen.Player.route)
                        }
                    )
                    BottomBar(navController, currentDestination)
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                content()
            }
        }
    }
}
