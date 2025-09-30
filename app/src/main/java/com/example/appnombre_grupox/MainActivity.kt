package com.example.appnombre_grupox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appnombre_grupox.navigation.AppNavigation
import com.example.appnombre_grupox.navigation.NavigationEvent
import com.example.appnombre_grupox.navigation.Screen
import com.example.appnombre_grupox.ui.screens.HomeScreen
import com.example.appnombre_grupox.ui.screens.HomeScreenCompacta
import com.example.appnombre_grupox.ui.screens.HomeScreenMediana
import com.example.appnombre_grupox.ui.screens.ProfileScreen
import com.example.appnombre_grupox.ui.screens.RegistroScreen
import com.example.appnombre_grupox.ui.screens.ResumenScreen
import com.example.appnombre_grupox.ui.screens.SettingsScreen
import com.example.appnombre_grupox.ui.theme.AppNombre_GrupoXTheme
import com.example.appnombre_grupox.ui.utils.obtenerWindowSizeClass
import com.example.appnombre_grupox.viewmodel.MainViewModel
import com.example.appnombre_grupox.viewmodel.UsuarioViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNombre_GrupoXTheme {
                // Layout base con NavHost
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)){
                        AppNavigation()
                    }

                }
            }
        }
    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        AppNombre_GrupoXTheme {
            Greeting("Android")
        }
    }

}