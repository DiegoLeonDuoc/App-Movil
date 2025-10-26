package com.example.teamusic_grupo11.viewmodel

import android.app.Application
import android.content.Intent // <-- Importa
import android.net.Uri // <-- Importa
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamusic_grupo11.data.dataStore
import com.example.teamusic_grupo11.data.PROFILE_IMAGE_URI // <-- Importa la llave de la imagen
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
import com.example.teamusic_grupo11.ui.state.UsuarioUiState // <-- Importa el State
import kotlinx.coroutines.flow.update // <-- Importa

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()

    private val DARK_KEY = booleanPreferencesKey("dark_theme")
    private val _darkTheme = MutableStateFlow(false)
    val darkTheme: StateFlow<Boolean> = _darkTheme.asStateFlow()

    private val _uiState = MutableStateFlow(UsuarioUiState())
    val uiState: StateFlow<UsuarioUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getApplication<Application>().dataStore.data
                .map { prefs -> prefs[DARK_KEY] ?: false }
                .collect { savedValue ->
                    _darkTheme.value = savedValue
                }
        }

        viewModelScope.launch {
            getApplication<Application>().dataStore.data
                .map { prefs -> prefs[PROFILE_IMAGE_URI] }
                .collect { savedUriString ->
                    _uiState.update { it.copy(savedProfileImageUri = savedUriString) }
                }
        }
    }

    fun setDarkTheme(enabled: Boolean) {
        viewModelScope.launch {
            getApplication<Application>().dataStore.edit { prefs ->
                prefs[DARK_KEY] = enabled
            }
            _darkTheme.value = enabled
        }
    }

    fun onNewImageSelected(uri: Uri?) {
        _uiState.update { it.copy(newSelectedImageUri = uri) }
    }


    fun saveProfileImage() {
        val newUri = _uiState.value.newSelectedImageUri ?: return

        viewModelScope.launch {

            val context = getApplication<Application>().applicationContext
            context.contentResolver.takePersistableUriPermission(
                newUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            // 2. Guardamos la URI (como String) en DataStore
            getApplication<Application>().dataStore.edit { prefs ->
                prefs[PROFILE_IMAGE_URI] = newUri.toString()
            }

            // 3. Limpiamos la "nueva" imagen del estado
            _uiState.update { it.copy(newSelectedImageUri = null) }
        }
    }


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

}