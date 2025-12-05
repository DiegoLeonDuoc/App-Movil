package com.example.teamusic_grupo11.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.teamusic_grupo11.viewmodel.MainViewModel
import com.example.teamusic_grupo11.ui.components.DialogFuncionPendiente
import com.example.teamusic_grupo11.viewmodel.PlayerViewModel


@Composable
fun BibliotecaScreen(
    navController: NavController,
    viewModel: MainViewModel,
    playerViewModel: PlayerViewModel
) {

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination
    var showDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
                    .horizontalScroll(rememberScrollState())
    ) {
        val bloques = listOf("Lista de reproducción", "Canciones", "Álbumes", "Artistas")
        bloques.forEach { bloque ->
            Button(
                onClick = {
                    showDialog = true
                }
            ) {
                Text(bloque, color = MaterialTheme.colorScheme.onPrimary)
            }
        }

    }

    DialogFuncionPendiente(
        show = showDialog,
        onDismiss = { showDialog = false }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearanceScreen(navController: NavController, viewModel: MainViewModel) {
    val isDark by viewModel.darkTheme.collectAsState(initial = false)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Apariencia") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
            Row(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.Companion.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Modo oscuro")
                Switch(
                    checked = isDark,
                    onCheckedChange = { checked ->
                        viewModel.setDarkTheme(checked)
                    }
                )
            }
        }
    }
}