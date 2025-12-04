package com.example.teamusic_grupo11.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamusic_grupo11.data.mappers.YouTubeMapper
import com.example.teamusic_grupo11.data.repository.YouTubeRepository
import com.example.teamusic_grupo11.dataDAO.Song
import com.example.teamusic_grupo11.network.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MusicUiState(
    val isLoading: Boolean = false,
    val trendingSongs: List<Song> = emptyList(),
    val recommendedSongs: List<Song> = emptyList(),
    val searchResults: List<Song> = emptyList(),
    val errorMessage: String? = null
)

// MusicViewModel es el intermediario entre la UI (pantallas) y los datos (Repositorio).
// Se encarga de gestionar el estado de la pantalla (MusicUiState) y de llamar al repositorio para obtener datos.
// Sobrevive a cambios de configuración (como rotar la pantalla).
class MusicViewModel(
    private val repository: YouTubeRepository = YouTubeRepository()
) : ViewModel() {
    
    // _uiState es mutable y privado, solo el ViewModel puede modificarlo.
    private val _uiState = MutableStateFlow(MusicUiState())
    
    // uiState es inmutable y público, la UI se suscribe a él para reaccionar a los cambios.
    // Contiene listas de canciones, estado de carga y mensajes de error.
    val uiState: StateFlow<MusicUiState> = _uiState.asStateFlow()
    
    init {
        // Cargar datos iniciales al crear el ViewModel
        loadTrending()
        loadRecommended()
    }
    
    fun loadTrending() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            when (val result = repository.getTrending()) {
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
}
