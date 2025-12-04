package com.example.teamusic_grupo11.network

object ApiConfig {
    const val BASE_URL = "https://www.googleapis.com/youtube/v3/"
    
    // API key loaded from BuildConfig (injected from env at build time)
    val API_KEY: String = com.example.teamusic_grupo11.BuildConfig.YOUTUBE_API_KEY
    
    const val TIMEOUT_SECONDS = 30L

}
