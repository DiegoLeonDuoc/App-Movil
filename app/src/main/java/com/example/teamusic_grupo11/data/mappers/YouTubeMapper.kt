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
        return Song(
            title = item.snippet.title,
            artist = item.snippet.channelTitle,
            imageRes = item.snippet.thumbnails.high?.url 
                ?: item.snippet.thumbnails.medium?.url 
                ?: item.snippet.thumbnails.default?.url 
                ?: ""
        )
    }
    
    fun fromVideoItem(item: YouTubeVideoItem): Song {
        return Song(
            title = item.snippet.title,
            artist = item.snippet.channelTitle,
            imageRes = item.snippet.thumbnails.high?.url 
                ?: item.snippet.thumbnails.medium?.url 
                ?: item.snippet.thumbnails.default?.url 
                ?: ""
        )
    }
    
    fun fromSearchList(items: List<YouTubeSearchItem>): List<Song> {
        return items.mapNotNull { fromSearchItem(it) }
    }
    
    fun fromVideoList(items: List<YouTubeVideoItem>): List<Song> {
        return items.map { fromVideoItem(it) }
    }
}
