package com.example.teamusic_grupo11.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    
    private var retrofit: Retrofit? = null
    
    // Configura y crea una instancia de OkHttpClient.
    // OkHttpClient es el motor que realiza las peticiones HTTP reales (como un navegador web invisible).
    private fun getOkHttpClient(): OkHttpClient {
        // Interceptor para registrar (log) los detalles de las peticiones y respuestas en la consola.
        // Útil para depuración (ver qué se envía y qué se recibe).
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        // Interceptor de autenticación para añadir la API Key a todas las peticiones
        val authInterceptor = Interceptor { chain ->
            val original = chain.request()
            val originalHttpUrl = original.url
            
            // Agregar la clave de API como parámetro de consulta (query parameter)
            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("key", ApiConfig.API_KEY)
                .build()
            
            val requestBuilder = original.newBuilder()
                .url(url)
            
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        
        // .Builder() es un patrón de diseño que permite configurar paso a paso el cliente antes de crearlo.
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor) // Añade el interceptor de autenticación
            .addInterceptor(loggingInterceptor) // Añade el interceptor de logs
            .connectTimeout(ApiConfig.TIMEOUT_SECONDS, TimeUnit.SECONDS) // Tiempo máximo para conectar
            .readTimeout(ApiConfig.TIMEOUT_SECONDS, TimeUnit.SECONDS)    // Tiempo máximo para recibir datos
            .writeTimeout(ApiConfig.TIMEOUT_SECONDS, TimeUnit.SECONDS)   // Tiempo máximo para enviar datos
            .build() // Finalmente construye la instancia de OkHttpClient con toda la configuración
    }
    
    // Crea y configura la instancia de Retrofit.
    // Retrofit convierte nuestra interfaz de Kotlin (YouTubeApiService) en llamadas HTTP reales usando OkHttpClient.
    fun getClient(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URL) // La URL base de la API (ej. https://www.googleapis.com/youtube/v3/)
                .client(getOkHttpClient())   // Usa nuestro cliente OkHttp configurado
                .addConverterFactory(GsonConverterFactory.create()) // Convierte el JSON de respuesta a objetos Kotlin automáticamente
                .build()
        }
        return retrofit!!
    }
    
    // Instancia "lazy" (perezosa) del servicio de la API.
    // Solo se crea la primera vez que se llama a 'ApiClient.apiService'.
    // Esta es la variable que usaremos en toda la app para hacer las llamadas.
    val apiService: YouTubeApiService by lazy {
        getClient().create(YouTubeApiService::class.java)
    }
}
