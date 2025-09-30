package com.example.appnombre_grupox.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appnombre_grupox.navigation.Screen
import com.example.appnombre_grupox.viewmodel.MainViewModel

@Composable
fun SettingsScreen(
    navController: NavController,   // Controlador de navegación para moverse entre pantallas
    viewModel: MainViewModel        // ViewModel que centraliza la navegación
) {
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