package com.example.teamusic_grupo11.viewmodel

import androidx.lifecycle.ViewModel
import com.example.teamusic_grupo11.ui.state.UsuarioUiState
import com.example.teamusic_grupo11.ui.validation.UsuarioErrores
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class UsuarioViewModel: ViewModel() {

    // Estado interno mutable
    private val _estado = MutableStateFlow(UsuarioUiState())

    // Estado expuesto a la UI
    val estado: StateFlow<UsuarioUiState> = _estado

    // Actualiza el campo nombre y limpia su error
    fun onNombreChange(valor:String){
        _estado.update {
            it.copy(
                nombre = valor,
                errores = it.errores.copy(nombre = null)
            )
        }
    }

    // Actualiza el campo correo y limpia su error
    fun onCorreoChange(valor:String){
        _estado.update {
            it.copy(
                correo = valor,
                errores = it.errores.copy(correo = null)
            )
        }
    }

    // Actualiza el campo clave y limpia su error
    fun onClaveChange(valor:String){
        _estado.update {
            it.copy(
                clave = valor,
                errores = it.errores.copy(clave = null)
            )
        }
    }

    // Actualiza el campo direccion y limpia su error
    fun onDireccionChange(valor:String){
        _estado.update {
            it.copy(
                direccion = valor,
                errores = it.errores.copy(direccion = null)
            )
        }
    }

    // Actualiza el checkbox de términos y condiciones
    fun onAceptarTerminosChange(valor:Boolean){
        _estado.update {
            it.copy(
                aceptaTerminos = valor
            )
        }
    }

    // Valida el formulario válido
    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value
        val errores = UsuarioErrores(
            nombre = if (estadoActual.nombre.isBlank()) "El nombre es requerido" else null,
            correo = if (estadoActual.correo.isBlank()) "El correo es requerido" else if (!estadoActual.correo.contains("@")) "El correo no es válido" else null,
            clave = if (estadoActual.clave.isBlank()) "La clave es requerida" else if (estadoActual.clave.length < 6) "La clave debe tener al menos 6 caracteres" else null,
            direccion = if (estadoActual.direccion.isBlank()) "La dirección es requerida" else null
        )

        val hayErrores = listOfNotNull(
            errores.nombre,
            errores.correo,
            errores.clave,
            errores.direccion
        ).isNotEmpty()

        _estado.update { it.copy(errores = errores) }

        return !hayErrores
    }

}