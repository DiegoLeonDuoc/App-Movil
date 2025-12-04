package com.example.teamusic_grupo11.data.models

import com.google.gson.annotations.SerializedName

// Envoltorios de respuesta de la API para YouTube Data API v3
// Estas clases representan la respuesta raíz que devuelve la API.

data class SearchResponse(
    @SerializedName("kind")
    val kind: String, // Tipo de recurso (ej. "youtube#searchListResponse")
    
    @SerializedName("etag")
    val etag: String, // Etiqueta de entidad para caché
    
    @SerializedName("nextPageToken")
    val nextPageToken: String? = null, // Token para pedir la siguiente página de resultados
    
    @SerializedName("regionCode")
    val regionCode: String? = null,
    
    @SerializedName("pageInfo")
    val pageInfo: PageInfo, // Información de paginación
    
    @SerializedName("items")
    val items: List<YouTubeSearchItem> // La lista real de resultados de búsqueda
)

data class VideoListResponse(
    @SerializedName("kind")
    val kind: String,
    
    @SerializedName("etag")
    val etag: String,
    
    @SerializedName("items")
    val items: List<YouTubeVideoItem>,
    
    @SerializedName("pageInfo")
    val pageInfo: PageInfo
)

data class PageInfo(
    @SerializedName("totalResults")
    val totalResults: Int,
    
    @SerializedName("resultsPerPage")
    val resultsPerPage: Int
)
