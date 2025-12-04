package com.example.teamusic_grupo11.network

import com.example.teamusic_grupo11.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface YouTubeApiService {
    
    // Search for music (videos in music category)
    @GET("search")
    suspend fun searchMusic(
        @Query("q") query: String,
        @Query("part") part: String = "snippet",
        @Query("type") type: String = "video",
        @Query("videoCategoryId") videoCategoryId: String = "10", // 10 is Music
        @Query("maxResults") limit: Int = 20
    ): Response<SearchResponse>
    
    // Get trending music (most popular videos in music category)
    @GET("videos")
    suspend fun getTrending(
        @Query("part") part: String = "snippet,contentDetails,statistics",
        @Query("chart") chart: String = "mostPopular",
        @Query("videoCategoryId") videoCategoryId: String = "10", // 10 is Music
        @Query("maxResults") limit: Int = 20,
        @Query("regionCode") regionCode: String = "US"
    ): Response<VideoListResponse>
    
    // For recommended, we'll use a general music search for now as "recommended" requires OAuth
    @GET("search")
    suspend fun getRecommended(
        @Query("q") query: String = "music",
        @Query("part") part: String = "snippet",
        @Query("type") type: String = "video",
        @Query("videoCategoryId") videoCategoryId: String = "10",
        @Query("maxResults") limit: Int = 20,
        @Query("order") order: String = "viewCount"
    ): Response<SearchResponse> 
    
    // Corrected getTrackDetails to use Query param 'id'
    @GET("videos")
    suspend fun getVideoDetails(
        @Query("id") id: String,
        @Query("part") part: String = "snippet,contentDetails,statistics"
    ): Response<VideoListResponse>
}
