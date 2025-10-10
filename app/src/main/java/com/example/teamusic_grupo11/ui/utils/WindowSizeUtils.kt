package com.example.teamusic_grupo11.ui.utils

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
// Paquete para obetener tamaño de pantalla
// Utilizado para definir una pantalla diferente por dispositivo
// INUTILIZADO
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun obtenerWindowSizeClass(): WindowSizeClass {
    //return calculateWindowSizeClass(LocalContext.current as android.app.Activity)
    val context = LocalContext.current
    val activity = context.getActivity()
    return calculateWindowSizeClass(activity as android.app.Activity)
}
@Composable
fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}