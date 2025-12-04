package com.example.teamusic_grupo11.network

object ApiConfig {
    const val BASE_URL = "https://www.googleapis.com/youtube/v3/"
    
    // API key loaded from BuildConfig (injected from env at build time)
    val API_KEY: String = com.example.teamusic_grupo11.BuildConfig.YOUTUBE_API_KEY
    
    const val TIMEOUT_SECONDS = 30L
    const val CACHE_SIZE = 10 * 1024 * 1024L // 10 MB
    
    init {
        if (API_KEY.isBlank()) {
            android.util.Log.e("ApiConfig", "CRITICAL: API Key is EMPTY! Check your build configuration.")
        } else {
            val maskedKey = if (API_KEY.length > 8) "${API_KEY.take(4)}...${API_KEY.takeLast(4)}" else "INVALID_LENGTH"
            android.util.Log.i("ApiConfig", "API Key loaded successfully: $maskedKey")
        }
    }
    
    fun isApiKeyValid(): Boolean = API_KEY.isNotBlank()
}
