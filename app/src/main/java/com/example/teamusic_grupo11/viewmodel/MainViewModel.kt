package com.example.teamusic_grupo11.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamusic_grupo11.data.dataStore
import com.example.teamusic_grupo11.navigation.NavigationEvent
import com.example.teamusic_grupo11.navigation.Screen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()

    // Key para DataStore
    private val DARK_KEY = booleanPreferencesKey("dark_theme")

    // Estado del tema expuesto como StateFlow
    private val _darkTheme = MutableStateFlow(false)
    val darkTheme: StateFlow<Boolean> = _darkTheme.asStateFlow()

    init {
        // Lee el valor guardado en DataStore y alimenta el StateFlow
        viewModelScope.launch {
            getApplication<Application>().dataStore.data
                .map { prefs -> prefs[DARK_KEY] ?: false }
                .collect { savedValue ->
                    _darkTheme.value = savedValue
                }
        }
    }

    // Cambiar el tema: guarda en DataStore (persistente)
    fun setDarkTheme(enabled: Boolean) {
        viewModelScope.launch {
            getApplication<Application>().dataStore.edit { prefs ->
                prefs[DARK_KEY] = enabled
            }
            // actualizar el StateFlow inmediatamente
            _darkTheme.value = enabled
        }
    }

    fun toggleTheme() {
        setDarkTheme(!_darkTheme.value)
    }

    // Navegación (tal como ya tenías)
    fun navigateTo(screen: Screen) {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.NavigateTo(route = screen))
        }
    }

    fun navigateBack() {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.PopBackStack)
        }
    }

    fun navigateUp() {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.NavigateUp)
        }
    }
}