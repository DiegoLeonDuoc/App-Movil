package com.example.teamusic_grupo11.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import androidx.compose.foundation.Image
import com.example.teamusic_grupo11.ui.state.UsuarioUiState
import com.example.teamusic_grupo11.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    viewModel: MainViewModel,
    uiState: UsuarioUiState
) {

    // Usamos OpenDocument para obtener una URI persistente.
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            // Cuando el usuario elige, se lo pasamos al ViewModel
            viewModel.onNewImageSelected(uri)
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    // Usamos el navigateBack del ViewModel
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(32.dp))



            // 2. Definimos qué modelo de imagen tenemos (puede ser Uri o String)
            val imageModel = uiState.newSelectedImageUri ?: uiState.savedProfileImageUri

            // 3. Modificador común para ambos casos
            val imageModifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)

            if (imageModel != null) {
                // Si SÍ tenemos una imagen (Uri o String), usamos AsyncImage
                AsyncImage(
                    model = imageModel,
                    contentDescription = "Foto de perfil",
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop
                )
            } else {
                // Si NO tenemos imagen, mostramos el ÍCONO por defecto
                Image(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Foto de perfil",
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón para Cambiar
            Button(onClick = {
                pickImageLauncher.launch(arrayOf("image/*")) // Solo imágenes
            }) {
                Text("Cambiar Foto")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de Guardado
            Button(
                onClick = {
                    viewModel.saveProfileImage()
                },
                enabled = uiState.newSelectedImageUri != null // Solo activo si hay foto nueva
            ) {
                Text("Guardar Cambios")
            }
        }
    }
}