package com.example.teamusic_grupo11.network

import com.example.teamusic_grupo11.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface YouTubeApiService {
    
    // @GET("search") indica que haremos una petición HTTP GET al endpoint "search" (la URL final será BASE_URL + "search").
    // @Query añade parámetros a la URL (ej. ?q=query&part=snippet...).
    // Se usan muchos @Query porque la API de YouTube requiere especificar qué partes de la información queremos (part),
    // el tipo de resultado (type), la categoría (videoCategoryId), etc., para filtrar y optimizar la respuesta.
    
    // Buscar música (videos en la categoría de música)
    @GET("search")
    suspend fun searchMusic(
        @Query("q") query: String, // El texto a buscar
        @Query("part") part: String = "snippet", // "snippet" trae título, descripción, imágenes, etc.
        @Query("type") type: String = "video", // Solo queremos videos
        @Query("videoCategoryId") videoCategoryId: String = "10", // 10 corresponde a la categoría de Música
        @Query("maxResults") limit: Int = 20 // Cantidad máxima de resultados
    ): Response<SearchResponse>
    
    // Obtener música en tendencia (videos más populares en la categoría de música)
    // Usamos el endpoint "videos" con chart="mostPopular".
    @GET("videos")
    suspend fun getTrending(
        @Query("part") part: String = "snippet,contentDetails,statistics", // Pedimos más detalles como estadísticas
        @Query("chart") chart: String = "mostPopular", // Lista de populares
        @Query("videoCategoryId") videoCategoryId: String = "10", // 10 corresponde a la categoría de Música
        @Query("maxResults") limit: Int = 20,
        @Query("regionCode") regionCode: String = "CL" // Código de región para las tendencias
    ): Response<VideoListResponse>
    
    // Para recomendaciones, usamos una búsqueda general de música ya que las recomendaciones personalizadas requieren OAuth
    @GET("search")
    suspend fun getRecommended(
        @Query("q") query: String = "music",
        @Query("part") part: String = "snippet",
        @Query("type") type: String = "video",
        @Query("videoCategoryId") videoCategoryId: String = "10",
        @Query("maxResults") limit: Int = 20,
        @Query("order") order: String = "viewCount" // Ordenamos por vistas para simular "recomendados"
    ): Response<SearchResponse> 
    
    // Obtener detalles de un video específico usando el parámetro 'id'
    @GET("videos")
    suspend fun getVideoDetails(
        @Query("id") id: String, // ID único del video
        @Query("part") part: String = "snippet,contentDetails,statistics"
    ): Response<VideoListResponse>
}
