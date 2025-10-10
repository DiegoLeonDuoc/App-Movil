package com.example.teamusic_grupo11.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.teamusic_grupo11.navigation.Screen

// BottomBar.kt
@Composable
fun BottomBar(
    currentDestination: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar {
        val items = listOf(
            BottomNavItem(
                route = Screen.Home.route,
                title = "Inicio",
                icon = Icons.Default.Home,
                contentDescription = "Pantalla de inicio"
            ),
            BottomNavItem(
                route = Screen.Explore.route,
                title = "Explorar",
                icon = Icons.Default.Search,
                contentDescription = "Pantalla de exploración"
            ),
            BottomNavItem(
                route = Screen.Profile.route,
                title = "Perfil",
                icon = Icons.Default.Person,
                contentDescription = "Pantalla de perfil"
            )
        )

        items.forEach { item ->
            NavigationBarItem(
                selected = currentDestination == item.route,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.contentDescription
                    )
                },
                label = { Text(text = item.title) },
                alwaysShowLabel = true
            )
        }
    }
}

data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val contentDescription: String
)