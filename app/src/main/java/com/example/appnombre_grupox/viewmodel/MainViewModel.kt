package com.example.appnombre_grupox.viewmodel

import androidx.lifecycle.ViewModel
import com.example.appnombre_grupox.navigation.NavigationEvent
import com.example.appnombre_grupox.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()

    // 'recieveAsFlow()': Expone el Channel como un Flow de solo lectura para que la UI pueda observarlo
    // Esto previene que la UI pueda enviar enventos al Channel directamente
    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()

    // Función que emite el evento de navegación hacia la ruta deseada
    fun navigateTo(screen: Screen) {
        CoroutineScope(Dispatchers.Main).launch {
            _navigationEvents.emit(NavigationEvent.NavigateTo(route = screen))
        }
    }

    // Función para volver atrás
    fun navigateBack() {
        CoroutineScope(Dispatchers.Main).launch {
            _navigationEvents.emit(NavigationEvent.PopBackStack)
        }
    }

    // Función para navegar arriba
    fun navigateUp() {
        CoroutineScope(Dispatchers.Main).launch {
            _navigationEvents.emit(NavigationEvent.NavigateUp)
        }
    }
}