package com.example.teamusic_grupo11.dataDAO

data class Song(
    val title: String,
    val artist: String,
    val imageRes: String, // or use a URL if online
    val audioUrl: String, // URL del archivo de audio
    val duration: Long = 0L // Duración en milisegundos (opcional)
)