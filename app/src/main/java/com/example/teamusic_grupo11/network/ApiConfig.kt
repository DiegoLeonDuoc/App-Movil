package com.example.teamusic_grupo11.network

object ApiConfig {
    const val BASE_URL = "https://www.googleapis.com/youtube/v3/"
    
    // Clave de API cargada desde BuildConfig (inyectada desde variables de entorno en tiempo de compilación)
    val API_KEY: String = com.example.teamusic_grupo11.BuildConfig.YOUTUBE_API_KEY
    
    const val TIMEOUT_SECONDS = 30L
    const val CACHE_SIZE = 10 * 1024 * 1024L // 10 MB
    
    fun isApiKeyValid(): Boolean = API_KEY.isNotBlank()
}
