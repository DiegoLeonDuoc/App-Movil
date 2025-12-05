package com.example.teamusic_grupo11.data.mappers

import com.example.teamusic_grupo11.data.models.YouTubeSearchItem
import com.example.teamusic_grupo11.data.models.YouTubeVideoItem
import com.example.teamusic_grupo11.dataDAO.Song

/**
 * Mapeador para convertir los modelos de la API de YouTube al modelo Song de la aplicación
 */
object YouTubeMapper {

    fun fromSearchItem(item: YouTubeSearchItem): Song? {
        val videoId = item.id.videoId ?: return null

        // Crear URL de audio basada en el ID del video de YouTube
        val audioUrl = buildAudioUrl(videoId)

        return Song(
            title = item.snippet.title,
            artist = item.snippet.channelTitle,
            imageRes = item.snippet.thumbnails.high?.url
                ?: item.snippet.thumbnails.medium?.url
                ?: item.snippet.thumbnails.default?.url
                ?: "",
            audioUrl = audioUrl,  // ¡Agregado!
            duration = 0L  // YouTube no proporciona duración en search, puedes intentar obtenerla de otra forma
        )
    }

    fun fromVideoItem(item: YouTubeVideoItem): Song {
        // Crear URL de audio basada en el ID del video
        val audioUrl = buildAudioUrl(item.id)

        return Song(
            title = item.snippet.title,
            artist = item.snippet.channelTitle,
            imageRes = item.snippet.thumbnails.high?.url
                ?: item.snippet.thumbnails.medium?.url
                ?: item.snippet.thumbnails.default?.url
                ?: "",
            audioUrl = audioUrl,  // ¡Agregado!
            duration = item.contentDetails?.duration?.toMillis() ?: 0L  // Convertir duración ISO 8601 a milisegundos
        )
    }

    fun fromSearchList(items: List<YouTubeSearchItem>): List<Song> {
        return items.mapNotNull { fromSearchItem(it) }
    }

    fun fromVideoList(items: List<YouTubeVideoItem>): List<Song> {
        return items.map { fromVideoItem(it) }
    }

    /**
     * Construye una URL de audio basada en el ID del video de YouTube.
     * NOTA: Para producción, necesitarías usar una API que extraiga audio de YouTube.
     */
    private fun buildAudioUrl(videoId: String): String {
        // Esta es una URL de ejemplo. En producción necesitas:
        // 1. Usar YouTube Data API para obtener información del video
        // 2. O usar un servicio que extraiga audio de YouTube
        // 3. O tener tus propios archivos de audio

        // Para pruebas, usa SoundHelix o similares:
        return "https://www.youtube.com/watch?v=$videoId"

        // O alternativamente para pruebas con archivos de audio reales:
        // return "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-${(1..8).random()}.mp3"
    }

    /**
     * Helper para convertir duración ISO 8601 (PT1H2M30S) a milisegundos
     */
    private fun String.toMillis(): Long {
        var duration = 0L
        var currentValue = 0L

        for (char in this) {
            when {
                char.isDigit() -> {
                    currentValue = currentValue * 10 + char.digitToInt()
                }
                char == 'H' -> {
                    duration += currentValue * 60 * 60 * 1000
                    currentValue = 0
                }
                char == 'M' -> {
                    duration += currentValue * 60 * 1000
                    currentValue = 0
                }
                char == 'S' -> {
                    duration += currentValue * 1000
                    currentValue = 0
                }
            }
        }

        return duration
    }
}