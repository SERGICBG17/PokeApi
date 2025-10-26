package com.example.ejemploapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ejemploapi.core.navegation.NavigationWrapper
import com.example.ejemploapi.ui.theme.EjemploApiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EjemploApiTheme {
                // Lanzamos el sistema de navegación
                NavigationWrapper()
            }
        }
    }
}
