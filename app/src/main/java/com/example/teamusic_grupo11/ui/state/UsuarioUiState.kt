package com.example.teamusic_grupo11.ui.state

import android.net.Uri
import com.example.teamusic_grupo11.ui.validation.UsuarioErrores

data class UsuarioUiState(
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val direccion: String = "",
    val aceptaTerminos: Boolean = false,
    val errores: UsuarioErrores = UsuarioErrores(),
    val savedProfileImageUri: String? = null,
    val newSelectedImageUri: Uri? = null
)