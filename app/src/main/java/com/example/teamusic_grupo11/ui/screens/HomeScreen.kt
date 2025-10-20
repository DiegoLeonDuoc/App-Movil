package com.example.teamusic_grupo11.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.teamusic_grupo11.data.Songs
import com.example.teamusic_grupo11.navigation.Screen
import com.example.teamusic_grupo11.viewmodel.MainViewModel
import kotlinx.coroutines.launch

import com.example.teamusic_grupo11.ui.components.BottomBar
import com.example.teamusic_grupo11.ui.components.GridCanciones
import com.example.teamusic_grupo11.ui.components.RowCanciones
import com.example.teamusic_grupo11.ui.components.RowCancionesDoble
import com.example.teamusic_grupo11.ui.components.TopBar
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel(),
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

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
                TopBar(viewModel, drawerState, scope)
            },
            bottomBar = {
                BottomBar(navController, currentDestination)
            }
        ) { innerPadding ->
            LazyColumn(
                contentPadding = innerPadding,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Text(
                        text = "Bienvenido!",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Text(
                        text = "Selección rápida",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                item {
                    GridCanciones(Songs()) { clickedSong ->
                        Log.d("SongClick", "Clicked ${clickedSong.title}")
                    }
                }

                item {
                    Text(
                        text = "Seguir escuchando",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                item {
                    RowCancionesDoble(Songs(), size = 100.dp) { clickedSong ->
                        Log.d("SongClick", "Clicked ${clickedSong.title}")
                    }
                }

                item {
                    Text(
                        text = "Recomendados",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                item {
                    RowCanciones(Songs(), size = 100.dp) { clickedSong ->
                        Log.d("SongClick", "Clicked ${clickedSong.title}")
                    }
                }

                item {
                    RowCanciones(Songs(), size = 100.dp) { clickedSong ->
                        Log.d("SongClick", "Clicked ${clickedSong.title}")
                    }
                }



                // Optional spacer at the bottom
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}
