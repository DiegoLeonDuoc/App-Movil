package com.example.appnombre_grupox.ui.state

import com.example.appnombre_grupox.ui.validation.UsuarioErrores

data class UsuarioUiState(
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val direccion: String = "",
    val aceptaTerminos: Boolean = false,
    val errores: UsuarioErrores = UsuarioErrores()
)