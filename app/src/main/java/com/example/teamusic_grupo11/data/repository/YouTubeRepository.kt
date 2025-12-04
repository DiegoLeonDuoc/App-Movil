package com.example.teamusic_grupo11.data.repository

import com.example.teamusic_grupo11.data.models.*
import com.example.teamusic_grupo11.network.ApiClient
import com.example.teamusic_grupo11.network.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class YouTubeRepository(
    private val apiService: com.example.teamusic_grupo11.network.YouTubeApiService = com.example.teamusic_grupo11.network.ApiClient.apiService
) {
    
    // Helper function to handle API responses
    private suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiCall()
                if (response.isSuccessful) {
                    response.body()?.let {
                        NetworkResult.Success(it)
                    } ?: NetworkResult.Error("Empty response body")
                } else {
                    NetworkResult.Error(
                        message = response.message(),
                        code = response.code()
                    )
                }
            } catch (e: Exception) {
                NetworkResult.Error(
                    message = e.message ?: "Unknown error occurred"
                )
            }
        }
    }
    
    // Music operations
    suspend fun searchMusic(query: String, limit: Int = 20): NetworkResult<SearchResponse> {
        return safeApiCall { apiService.searchMusic(query, limit = limit) }
    }
    
    suspend fun getTrending(): NetworkResult<VideoListResponse> {
        return safeApiCall { apiService.getTrending() }
    }
    
    suspend fun getRecommended(): NetworkResult<SearchResponse> {
        return safeApiCall { apiService.getRecommended() }
    }
    
    suspend fun getTrackDetails(trackId: String): NetworkResult<VideoListResponse> {
        return safeApiCall { apiService.getVideoDetails(id = trackId) }
    }
}
