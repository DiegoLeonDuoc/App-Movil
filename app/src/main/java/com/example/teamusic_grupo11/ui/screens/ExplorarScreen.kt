package com.example.teamusic_grupo11.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.teamusic_grupo11.navigation.Screen
import com.example.teamusic_grupo11.viewmodel.MainViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ExplorarScreen(
    navController: NavController,
    viewModel: MainViewModel
) {

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    // Estructura visual centralizada
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título
        Text(text = "Pantalla de configuración")
        Spacer(modifier = Modifier.height(24.dp))

        // Botón para volver a home
        Button(
            onClick = {
                viewModel.navigateTo(Screen.Home)
            }
        ) {
            Text("Volver al Inicio")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Botón para ir al perfil
        Button(
            onClick = {
                viewModel.navigateTo(Screen.Profile)
            }
        ) {
            Text("Ir al Perfil")
        }
    }
}
