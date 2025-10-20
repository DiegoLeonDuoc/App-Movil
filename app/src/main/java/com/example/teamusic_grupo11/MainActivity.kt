package com.example.teamusic_grupo11

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.teamusic_grupo11.navigation.AppNavigation
import com.example.teamusic_grupo11.ui.theme.TeaMusic_Grupo11Theme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TeaMusic_Grupo11Theme {
                AppNavigation()
            }
        }
    }
}

