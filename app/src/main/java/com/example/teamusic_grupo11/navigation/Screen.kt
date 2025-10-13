package com.example.teamusic_grupo11.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

// Sealed class para definir rutas tipo-safe en la navegación
sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    data object Home : Screen(route = "home", label = "Inicio", icon = Icons.Default.Home)
    data object Profile : Screen(route = "profile", label = "Perfil", icon = Icons.Default.Person)
    data object Settings : Screen(route = "settings", label = "Configuración", icon = Icons.Default.Settings)
    data object Explore : Screen(route = "explorar", label = "Explorar", icon = Icons.Default.Explore)
    data object Resumen : Screen(route = "resumen", label = "Resumen", icon = Icons.Default.Explore)
    data object Login : Screen(route = "login", label = "Ingreso", icon = Icons.AutoMirrored.Default.Login)
    data object Registro : Screen(route = "registro ", label = "Registro", icon = Icons.AutoMirrored.Default.Login)


//    data class Detail(val itemId: String) : Screen(route = "detail_page/{itemId}") {
//        // Función para construir la ruta final con el argumento.
//        // Este evita errores al crear la ruta string.
//        fun buildRoute(): String {
//            // Reemplaza el placeholder {itemId} en la ruta base con el valor real.
//            return route.replace(oldValue = "{itemId}", newValue = itemId)
//        }
//    }
//
//    // Si tuvieras más argumentos, se agregarían a la data class y a la cadena de ruta.
//    data class UserProfile(val userId: String, val userName: String) :
//        Screen("profile/{userId}?name={userName}") { }
}
