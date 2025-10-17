package com.example.teamusic_grupo11.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.teamusic_grupo11.navigation.Screen

// BottomBar.kt
@Composable
fun BottomBar(
    navController: NavController,
    currentDestination: NavDestination?
) {
    NavigationBar {
        val items = listOf(Screen.Home, Screen.Explore, Screen.Library)
        items.forEach { screen ->
            NavigationBarItem(
                selected = currentDestination?.route == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.label
                    )
                },
                label = { Text(text = screen.label) }
            )
        }
    }
}