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

class MusicViewModel(
    private val repository: YouTubeRepository = YouTubeRepository()
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MusicUiState())
    val uiState: StateFlow<MusicUiState> = _uiState.asStateFlow()
    
    init {
        // Load initial data
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
                    // Silently fail for recommended, keep existing data
                }
                is NetworkResult.Loading -> {
                    // No action needed
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
