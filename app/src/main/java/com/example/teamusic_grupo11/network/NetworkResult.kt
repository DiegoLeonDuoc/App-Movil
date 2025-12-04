package com.example.teamusic_grupo11.network

// NetworkResult es una clase sellada (sealed class) que representa los posibles estados de una operación de red.
// Permite manejar de forma estructurada el éxito, el error y la carga.
sealed class NetworkResult<out T> {
    // Estado de Éxito: contiene los datos (data) devueltos por la API.
    data class Success<out T>(val data: T) : NetworkResult<T>()
    
    // Estado de Error: contiene un mensaje de error y opcionalmente un código de error HTTP.
    data class Error(val message: String, val code: Int? = null) : NetworkResult<Nothing>()
    
    // Estado de Carga: indica que la operación está en curso.
    object Loading : NetworkResult<Nothing>()
}
