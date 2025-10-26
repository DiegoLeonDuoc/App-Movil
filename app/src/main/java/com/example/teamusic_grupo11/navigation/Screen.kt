package com.example.teamusic_grupo11.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Edit // <-- 1. IMPORTA EL ÍCONO DE EDITAR
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector


sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    data object Home : Screen(route = "home", label = "Inicio", icon = Icons.Default.Home)
    data object Profile : Screen(route = "profile", label = "Perfil", icon = Icons.Default.Person)
    data object Settings : Screen(route = "settings", label = "Configuración", icon = Icons.Default.Settings)
    data object Appearance : Screen(route = "appearance", label = "Apariencia", icon = Icons.Default.Settings)
    data object Explore : Screen(route = "explorar", label = "Explorar", icon = Icons.Default.Explore)
    data object Resumen : Screen(route = "resumen", label = "Resumen", icon = Icons.Default.Explore)
    data object Login : Screen(route = "login", label = "Ingreso", icon = Icons.AutoMirrored.Default.Login)


    data object Registro : Screen(route = "registro", label = "Registro", icon = Icons.AutoMirrored.Default.Login)
    data object Library: Screen(route = "biblioteca", label = "Biblioteca", icon = Icons.Default.LibraryMusic)


    data object ProfileEdit : Screen(route = "profile_edit", label = "Editar Perfil", icon = Icons.Default.Edit)


}