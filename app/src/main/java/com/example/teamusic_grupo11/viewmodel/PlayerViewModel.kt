package com.example.teamusic_grupo11.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.teamusic_grupo11.dataDAO.Song
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PlayerState(
    val currentSong: Song? = null,
    val isPlaying: Boolean = false,
    val currentPosition: Float = 0f,
    val currentTimeMs: Long = 0L,
    val durationMs: Long = 0L,
    val isLoading: Boolean = false,
    val isBuffering: Boolean = false
)

class PlayerViewModel : ViewModel() {
    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    private var exoPlayer: ExoPlayer? = null
    private var updatePositionJob: Job? = null

    fun initializePlayer(context: Context) {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build().apply {
                // Configurar listeners
                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        when (playbackState) {
                            Player.STATE_BUFFERING -> {
                                _playerState.value = _playerState.value.copy(
                                    isBuffering = true,
                                    isLoading = true
                                )
                            }
                            Player.STATE_READY -> {
                                _playerState.value = _playerState.value.copy(
                                    isBuffering = false,
                                    isLoading = false
                                )

                                // Actualizar duración
                                val duration = exoPlayer?.duration ?: 0L
                                if (duration > 0) {
                                    _playerState.value = _playerState.value.copy(
                                        durationMs = duration
                                    )
                                }
                            }
                            Player.STATE_ENDED -> {
                                _playerState.value = _playerState.value.copy(
                                    isPlaying = false
                                )
                                stopPositionUpdates()
                            }
                        }
                    }

                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        _playerState.value = _playerState.value.copy(
                            isPlaying = isPlaying
                        )

                        if (isPlaying) {
                            startPositionUpdates()
                        } else {
                            stopPositionUpdates()
                        }
                    }
                })
            }
        }
    }

    fun playSong(song: Song) {
        exoPlayer?.let { player ->
            _playerState.value = _playerState.value.copy(
                currentSong = song,
                isLoading = true
            )

            // Crear y configurar el MediaItem
            val mediaItem = MediaItem.fromUri(song.audioUrl)
            player.setMediaItem(mediaItem)
            player.prepare()
            player.play()

            // Actualizar duración si está disponible en el objeto Song
            if (song.duration > 0) {
                _playerState.value = _playerState.value.copy(
                    durationMs = song.duration
                )
            }
        }
    }

    fun togglePlayPause() {
        exoPlayer?.let { player ->
            if (player.isPlaying) {
                player.pause()
            } else {
                if (player.playbackState == Player.STATE_ENDED ||
                    player.playbackState == Player.STATE_IDLE) {
                    // Si la canción terminó o no hay nada reproduciendo
                    // reproducir desde el inicio
                    player.seekTo(0)
                }
                player.play()
            }
        }
    }

    fun playNext() {
        // TODO: Implementar lógica de playlist
        // Por ahora, reiniciar la canción actual
        exoPlayer?.seekTo(0)
        exoPlayer?.play()
    }

    fun playPrevious() {
        // TODO: Implementar lógica de playlist
        // Por ahora, reiniciar la canción actual
        exoPlayer?.seekTo(0)
        exoPlayer?.play()
    }

    fun seekTo(position: Float) {
        exoPlayer?.let { player ->
            val duration = player.duration
            if (duration > 0) {
                val newPositionMs = (position * duration).toLong()
                player.seekTo(newPositionMs)

                _playerState.value = _playerState.value.copy(
                    currentPosition = position,
                    currentTimeMs = newPositionMs
                )
            }
        }
    }

    private fun startPositionUpdates() {
        stopPositionUpdates() // Detener cualquier job anterior

        updatePositionJob = viewModelScope.launch {
            while (true) {
                exoPlayer?.let { player ->
                    if (player.duration > 0) {
                        val currentPos = player.currentPosition
                        val duration = player.duration
                        val progress = if (duration > 0) {
                            currentPos.toFloat() / duration.toFloat()
                        } else 0f

                        _playerState.value = _playerState.value.copy(
                            currentPosition = progress,
                            currentTimeMs = currentPos
                        )
                    }
                }
                delay(100) // Actualizar cada 100ms
            }
        }
    }

    private fun stopPositionUpdates() {
        updatePositionJob?.cancel()
        updatePositionJob = null
    }

    fun releasePlayer() {
        stopPositionUpdates()
        exoPlayer?.release()
        exoPlayer = null
        _playerState.value = PlayerState()
    }

    fun getCurrentPosition(): Long {
        return exoPlayer?.currentPosition ?: 0L
    }

    fun getDuration(): Long {
        return exoPlayer?.duration ?: 0L
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }
}