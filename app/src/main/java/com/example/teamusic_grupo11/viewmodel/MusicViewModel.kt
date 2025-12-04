package com.example.teamusic_grupo11.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.datastore.preferences.core.edit
import com.example.teamusic_grupo11.data.SELECTED_REGION
import com.example.teamusic_grupo11.data.dataStore
import com.example.teamusic_grupo11.data.mappers.YouTubeMapper
import com.example.teamusic_grupo11.data.repository.YouTubeRepository
import com.example.teamusic_grupo11.dataDAO.Song
import com.example.teamusic_grupo11.network.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

data class MusicUiState(
    val isLoading: Boolean = false,
    val trendingSongs: List<Song> = emptyList(),
    val recommendedSongs: List<Song> = emptyList(),
    val searchResults: List<Song> = emptyList(),
    val errorMessage: String? = null,
    val selectedRegion: String = "CL" // Código de región por defecto (Chile)
)

// MusicViewModel es el intermediario entre la UI (pantallas) y los datos (Repositorio).
// Se encarga de gestionar el estado de la pantalla (MusicUiState) y de llamar al repositorio para obtener datos.
// Sobrevive a cambios de configuración (como rotar la pantalla).
class MusicViewModel(
    private val context: Context,
    private val repository: YouTubeRepository = YouTubeRepository()
) : ViewModel() {
    
    // _uiState es mutable y privado, solo el ViewModel puede modificarlo.
    private val _uiState = MutableStateFlow(MusicUiState())
    
    // uiState es inmutable y público, la UI se suscribe a él para reaccionar a los cambios.
    // Contiene listas de canciones, estado de carga y mensajes de error.
    val uiState: StateFlow<MusicUiState> = _uiState.asStateFlow()
    
    init {
        // Cargar región guardada y luego cargar datos iniciales
        viewModelScope.launch {
            loadSavedRegion()
            loadTrending()
            loadRecommended()
        }
    }
    
    fun loadTrending() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            // Usar la región seleccionada del estado
            val regionCode = _uiState.value.selectedRegion
            
            when (val result = repository.getTrending(regionCode)) {
                is NetworkResult.Success -> {
                    val songs = YouTubeMapper.fromVideoList(result.data.items)
                    _uiState.value = _uiState.value.copy(
                        trendingSongs = songs,
                        isLoading = false
                    )
                }
                is NetworkResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                is NetworkResult.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }
    
    fun loadRecommended() {
        viewModelScope.launch {
            when (val result = repository.getRecommended()) {
                is NetworkResult.Success -> {
                    val songs = YouTubeMapper.fromSearchList(result.data.items)
                    _uiState.value = _uiState.value.copy(
                        recommendedSongs = songs
                    )
                }
                is NetworkResult.Error -> {
                    // Fallar silenciosamente para recomendaciones, mantener datos existentes
                }
                is NetworkResult.Loading -> {
                    // No se requiere acción
                }
            }
        }
    }
    
    fun searchMusic(query: String) {
        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(searchResults = emptyList())
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            when (val result = repository.searchMusic(query)) {
                is NetworkResult.Success -> {
                    val songs = YouTubeMapper.fromSearchList(result.data.items)
                    _uiState.value = _uiState.value.copy(
                        searchResults = songs,
                        isLoading = false
                    )
                }
                is NetworkResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                is NetworkResult.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
    
    // ===== Funciones de Gestión de Región =====
    
    /**
     * Carga la región guardada desde DataStore.
     * Si no hay región guardada, usa "CL" (Chile) por defecto.
     */
    private suspend fun loadSavedRegion() {
        val savedRegion = context.dataStore.data
            .map { preferences ->
                preferences[SELECTED_REGION] ?: "CL"
            }
            .first()
        
        _uiState.value = _uiState.value.copy(selectedRegion = savedRegion)
    }
    
    /**
     * Guarda la región seleccionada en DataStore para persistencia permanente.
     */
    private suspend fun saveRegion(regionCode: String) {
        context.dataStore.edit { preferences ->
            preferences[SELECTED_REGION] = regionCode
        }
    }
    
    /**
     * Cambia la región seleccionada y recarga las tendencias.
     * La región se guarda permanentemente en DataStore.
     * 
     * @param regionCode Código ISO 3166-1 alpha-2 del país (ej: "US", "MX", "ES")
     */
    fun setRegion(regionCode: String) {
        viewModelScope.launch {
            // Actualizar el estado con la nueva región
            _uiState.value = _uiState.value.copy(selectedRegion = regionCode)
            
            // Guardar la región en DataStore
            saveRegion(regionCode)
            
            // Recargar las tendencias con la nueva región
            loadTrending()
        }
    }
}
