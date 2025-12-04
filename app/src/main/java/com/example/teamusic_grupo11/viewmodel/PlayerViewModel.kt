package com.example.teamusic_grupo11.viewmodel

import androidx.lifecycle.ViewModel
import com.example.teamusic_grupo11.dataDAO.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class PlayerState(
    val currentSong: Song? = null,
    val isPlaying: Boolean = false,
    val currentPosition: Float = 0f, // 0.0 to 1.0
    val currentTimeMs: Long = 0L,
    val durationMs: Long = 180000L // Default 3 minutes for demo
)

class PlayerViewModel : ViewModel() {
    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    fun playSong(song: Song) {
        _playerState.value = _playerState.value.copy(
            currentSong = song,
            isPlaying = true,
            currentPosition = 0f,
            currentTimeMs = 0L
        )
    }

    fun togglePlayPause() {
        _playerState.value = _playerState.value.copy(
            isPlaying = !_playerState.value.isPlaying
        )
    }

    fun playNext() {
        // TODO: Implement playlist logic
        // For now, just toggle play state
        _playerState.value = _playerState.value.copy(
            currentPosition = 0f,
            currentTimeMs = 0L
        )
    }

    fun playPrevious() {
        // TODO: Implement playlist logic
        // For now, just restart current song
        _playerState.value = _playerState.value.copy(
            currentPosition = 0f,
            currentTimeMs = 0L
        )
    }

    fun seekTo(position: Float) {
        val newTimeMs = (position * _playerState.value.durationMs).toLong()
        _playerState.value = _playerState.value.copy(
            currentPosition = position,
            currentTimeMs = newTimeMs
        )
    }
}
