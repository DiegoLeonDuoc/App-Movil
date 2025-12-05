package com.example.teamusic_grupo11

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teamusic_grupo11.navigation.AppNavigation
import com.example.teamusic_grupo11.ui.theme.TeaMusic_Grupo11Theme
import com.example.teamusic_grupo11.viewmodel.MainViewModel
import com.example.teamusic_grupo11.viewmodel.PlayerViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mainViewModel: MainViewModel = viewModel()
            val darkTheme by mainViewModel.darkTheme.collectAsState()

            // Obtener el PlayerViewModel
            val playerViewModel: PlayerViewModel = viewModel()

            // Inicializar el player cuando se crea la Activity
            LaunchedEffect(Unit) {
                playerViewModel.initializePlayer(this@MainActivity)
            }

            // Liberar el player cuando se destruye la Activity
            DisposableEffect(Unit) {
                onDispose {
                    playerViewModel.releasePlayer()
                }
            }

            TeaMusic_Grupo11Theme(darkTheme = darkTheme, dynamicColor = false) {
                // SOLO pasar mainViewModel ya que AppNavigation obtiene playerViewModel internamente
                AppNavigation(mainViewModel = mainViewModel)
            }
        }
    }
}