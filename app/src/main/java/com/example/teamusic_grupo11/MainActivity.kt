package com.example.teamusic_grupo11

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teamusic_grupo11.ui.theme.TeaMusic_Grupo11Theme
import com.example.teamusic_grupo11.navigation.AppNavigation
import com.example.teamusic_grupo11.viewmodel.MainViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Debug check for API Key
        if (!com.example.teamusic_grupo11.network.ApiConfig.isApiKeyValid()) {
            android.widget.Toast.makeText(this, "⚠️ API KEY MISSING!", android.widget.Toast.LENGTH_LONG).show()
        } else {
            android.widget.Toast.makeText(this, "✅ API Key Loaded", android.widget.Toast.LENGTH_SHORT).show()
        }
        
        setContent {
            val mainViewModel: MainViewModel = viewModel()
            val darkTheme by mainViewModel.darkTheme.collectAsState()

            TeaMusic_Grupo11Theme(darkTheme = darkTheme, dynamicColor = false) {

                AppNavigation(mainViewModel)
            }

        }
    }
}