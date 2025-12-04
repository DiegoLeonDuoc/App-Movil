package com.example.teamusic_grupo11.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    
    private var retrofit: Retrofit? = null
    
    private fun getOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        val authInterceptor = Interceptor { chain ->
            val original = chain.request()
            val originalHttpUrl = original.url
            
            // Add API key as query parameter
            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("key", ApiConfig.API_KEY)
                .build()
            
            val requestBuilder = original.newBuilder()
                .url(url)
            
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(ApiConfig.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(ApiConfig.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(ApiConfig.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }
    
    fun getClient(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URL)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
    
    val apiService: YouTubeApiService by lazy {
        getClient().create(YouTubeApiService::class.java)
    }
}
