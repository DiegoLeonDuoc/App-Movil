package com.example.teamusic_grupo11.data.models

import com.google.gson.annotations.SerializedName

// Modelos comunes para la API de Datos de YouTube v3
// Estos modelos reflejan la estructura exacta del JSON que devuelve YouTube.
// @SerializedName("nombre_json") mapea el campo del JSON a nuestra variable en Kotlin.

data class YouTubeSearchItem(
    @SerializedName("id")
    val id: YouTubeId, // Objeto ID que contiene videoId, channelId, etc.
    
    @SerializedName("snippet")
    val snippet: Snippet // "Snippet" es el resumen del video (título, descripción, etc.)
)

data class YouTubeVideoItem(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("snippet")
    val snippet: Snippet,
    
    @SerializedName("contentDetails")
    val contentDetails: ContentDetails? = null,
    
    @SerializedName("statistics")
    val statistics: Statistics? = null
)

data class YouTubeId(
    @SerializedName("kind")
    val kind: String,
    
    @SerializedName("videoId")
    val videoId: String? = null,
    
    @SerializedName("playlistId")
    val playlistId: String? = null,
    
    @SerializedName("channelId")
    val channelId: String? = null
)

data class Snippet(
    @SerializedName("publishedAt")
    val publishedAt: String,
    
    @SerializedName("channelId")
    val channelId: String,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("description")
    val description: String,
    
    @SerializedName("thumbnails")
    val thumbnails: Thumbnails,
    
    @SerializedName("channelTitle")
    val channelTitle: String,
    
    @SerializedName("resourceId")
    val resourceId: YouTubeId? = null
)

data class Thumbnails(
    @SerializedName("default")
    val default: Thumbnail? = null,
    
    @SerializedName("medium")
    val medium: Thumbnail? = null,
    
    @SerializedName("high")
    val high: Thumbnail? = null
)

data class Thumbnail(
    @SerializedName("url")
    val url: String,
    
    @SerializedName("width")
    val width: Int? = null,
    
    @SerializedName("height")
    val height: Int? = null
)

data class ContentDetails(
    @SerializedName("duration")
    val duration: String, // Formato ISO 8601, ej., PT1H2M10S
    
    @SerializedName("dimension")
    val dimension: String? = null,
    
    @SerializedName("definition")
    val definition: String? = null
)

data class Statistics(
    @SerializedName("viewCount")
    val viewCount: String? = null,
    
    @SerializedName("likeCount")
    val likeCount: String? = null,
    
    @SerializedName("favoriteCount")
    val favoriteCount: String? = null,
    
    @SerializedName("commentCount")
    val commentCount: String? = null
)
