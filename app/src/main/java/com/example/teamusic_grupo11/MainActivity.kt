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
        setContent {
            val mainViewModel: MainViewModel = viewModel()
            val darkTheme by mainViewModel.darkTheme.collectAsState()
            TeaMusic_Grupo11Theme(darkTheme = darkTheme, dynamicColor = false) {
                AppNavigation()
            }

            TeaMusic_Grupo11Theme(darkTheme = darkTheme, dynamicColor = false) {
                AppNavigation()
            }
        }
    }
}