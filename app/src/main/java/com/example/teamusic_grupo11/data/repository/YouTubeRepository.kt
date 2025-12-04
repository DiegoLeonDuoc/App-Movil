package com.example.teamusic_grupo11.data.repository

import com.example.teamusic_grupo11.data.models.*
import com.example.teamusic_grupo11.network.ApiClient
import com.example.teamusic_grupo11.network.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class YouTubeRepository(
    private val apiService: com.example.teamusic_grupo11.network.YouTubeApiService = ApiClient.apiService
) {
    
    // Función auxiliar para manejar las respuestas de la API de manera segura.
    // 'safeApiCall' envuelve la llamada a la red en un bloque try-catch y maneja los errores HTTP.
    // Recibe una función suspendida 'apiCall' que devuelve un 'Response<T>'.
    // Devuelve un 'NetworkResult<T>' que puede ser Success o Error.
    private suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
        // 'withContext(Dispatchers.IO)' asegura que la operación se ejecute en el hilo de E/S (Input/Output),
        // para no bloquear la interfaz de usuario.
        return withContext(Dispatchers.IO) {
            try {
                val response = apiCall() // Ejecuta la llamada a la API
                if (response.isSuccessful) {
                    // Si la respuesta es exitosa (código 200-299), devolvemos el cuerpo de la respuesta envuelto en Success.
                    // 'snippet' es una parte común de la respuesta de YouTube que contiene los metadatos básicos
                    // (título, descripción, miniaturas, etc.). Aunque aquí manejamos el objeto completo 'T'.
                    response.body()?.let {
                        NetworkResult.Success(it)
                    } ?: NetworkResult.Error("Cuerpo de respuesta vacío")
                } else {
                    // Si la respuesta no es exitosa (ej. 404, 500), devolvemos un Error con el mensaje y código.
                    NetworkResult.Error(
                        message = response.message(),
                        code = response.code()
                    )
                }
            } catch (e: Exception) {
                // Si ocurre una excepción (ej. sin internet), devolvemos un Error con el mensaje de la excepción.
                NetworkResult.Error(
                    message = e.message ?: "Ocurrió un error desconocido"
                )
            }
        }
    }
    
    // Operaciones relacionadas con música
    suspend fun searchMusic(query: String, limit: Int = 20): NetworkResult<SearchResponse> {
        return safeApiCall { apiService.searchMusic(query, limit = limit) }
    }
    
    suspend fun getTrending(regionCode: String = "CL"): NetworkResult<VideoListResponse> {
        return safeApiCall { apiService.getTrending(regionCode = regionCode) }
    }
    
    suspend fun getRecommended(): NetworkResult<SearchResponse> {
        return safeApiCall { apiService.getRecommended() }
    }
    
    suspend fun getTrackDetails(trackId: String): NetworkResult<VideoListResponse> {
        return safeApiCall { apiService.getVideoDetails(id = trackId) }
    }
}
