package com.example.teamusic_grupo11.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.teamusic_grupo11.navigation.Screen
import com.example.teamusic_grupo11.viewmodel.MainViewModel
import com.example.teamusic_grupo11.viewmodel.UsuarioViewModel

@Composable
fun RegistroScreen(
    navController: NavController,
    userViewModel: UsuarioViewModel,
    viewModel: MainViewModel = viewModel()
) {
    val estado by userViewModel.estado.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Campo nombre
        OutlinedTextField(
            value = estado.nombre,
            onValueChange = userViewModel::onNombreChange,
            label = { Text(text = "Nombre de usuario") },
            isError = estado.errores.nombre != null,
            supportingText = {
                estado.errores.nombre?.let {
                    Text(text = it, color = colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Campo clave
        OutlinedTextField(
            value = estado.clave,
            onValueChange = userViewModel::onClaveChange,
            label = { Text(text = "Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            isError = estado.errores.clave != null,
            supportingText = {
                estado.errores.clave?.let {
                    Text(text = it, color = colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Campo correo
        OutlinedTextField(
            value = estado.correo,
            onValueChange = userViewModel::onCorreoChange,
            label = { Text(text = "Correo") },
            isError = estado.errores.correo != null,
            supportingText = {
                estado.errores.correo?.let {
                    Text(text = it, color = colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Checkbox de términos y condiciones
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = estado.aceptaTerminos,
                onCheckedChange = userViewModel::onAceptarTerminosChange
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Acepto los términos y condiciones",
                style = MaterialTheme.typography.bodySmall
            )
        }

        // Botón enviar
        Button(
            onClick = {
                if (userViewModel.validarFormulario()) {
                    navController.navigate(Screen.Resumen.route)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Registrar")
        }
    }
}