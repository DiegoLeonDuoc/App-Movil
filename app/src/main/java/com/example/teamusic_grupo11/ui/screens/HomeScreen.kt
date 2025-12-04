package com.example.teamusic_grupo11.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.teamusic_grupo11.data.Songs
import com.example.teamusic_grupo11.viewmodel.MainViewModel
import com.example.teamusic_grupo11.ui.components.GridCanciones
import com.example.teamusic_grupo11.ui.components.RowCanciones
import com.example.teamusic_grupo11.ui.components.RowCancionesDoble
import com.example.teamusic_grupo11.ui.components.RegionSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel(),
    playerViewModel: com.example.teamusic_grupo11.viewmodel.PlayerViewModel
) {
    val context = LocalContext.current
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination
    
    // Crear MusicViewModel con Context usando factory
    val musicViewModel: com.example.teamusic_grupo11.viewmodel.MusicViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return com.example.teamusic_grupo11.viewmodel.MusicViewModel(context) as T
            }
        }
    )
    
    val musicState by musicViewModel.uiState.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        // Selector de región
        item {
            RegionSelector(
                selectedRegionCode = musicState.selectedRegion,
                onRegionSelected = { regionCode ->
                    musicViewModel.setRegion(regionCode)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
        
        item {
            Text(
                text = "Bienvenido!",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Show loading indicator
        if (musicState.isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        // Show error message
        if (musicState.errorMessage != null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Error",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Text(
                            text = musicState.errorMessage ?: "Unknown error",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Button(
                            onClick = { musicViewModel.clearError() },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("Dismiss")
                        }
                    }
                }
            }
        }

        // Trending section
        if (musicState.trendingSongs.isNotEmpty()) {
            item {
                Text(
                    text = "Tendencias",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 15.dp)
                )
            }

            item {
                GridCanciones(musicState.trendingSongs) { clickedSong ->
                    Log.d("SongClick", "Clicked ${clickedSong.title}")
                    playerViewModel.playSong(clickedSong)
                }
            }
        }

        // Recommended section
        if (musicState.recommendedSongs.isNotEmpty()) {
            item {
                Text(
                    text = "Recomendados",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 15.dp)
                )
            }

            item {
                RowCanciones(musicState.recommendedSongs, size = 100.dp) { clickedSong ->
                    Log.d("SongClick", "Clicked ${clickedSong.title}")
                    playerViewModel.playSong(clickedSong)
                }
            }
        }

        // Fallback to mock data if API data is not available
        if (!musicState.isLoading && musicState.trendingSongs.isEmpty() && musicState.recommendedSongs.isEmpty()) {
            item {
                Text(
                    text = "Selección rápida",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 15.dp)
                )
            }

            item {
                GridCanciones(Songs()) { clickedSong ->
                    Log.d("SongClick", "Clicked ${clickedSong.title}")
                    playerViewModel.playSong(clickedSong)
                }
            }

            item {
                Text(
                    text = "Seguir escuchando",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 15.dp)
                )
            }

            item {
                RowCancionesDoble(Songs(), size = 100.dp) { clickedSong ->
                    Log.d("SongClick", "Clicked ${clickedSong.title}")
                    playerViewModel.playSong(clickedSong)
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}