package com.example.teamusic_grupo11.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

// DataStoreModule.kt
// Este archivo configura DataStore, que es la solución moderna de Android para guardar datos simples (clave-valor).
// Reemplaza a SharedPreferences.

// Extensión para crear una única instancia de DataStore ligada al Contexto de la aplicación.
// "settings" es el nombre del archivo donde se guardarán los datos.
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

// Definición de las claves para guardar/leer datos.
// PROFILE_IMAGE_URI es la clave para guardar la URI de la imagen de perfil del usuario.
val PROFILE_IMAGE_URI = stringPreferencesKey("profile_image_uri")