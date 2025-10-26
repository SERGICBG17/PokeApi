package com.example.ejemploapi.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ejemploapi.R

@Composable
fun InicioScreen(
    onLoginClick: () -> Unit = {},
    onRegistroClick: () -> Unit = {}
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Imagen del logo
            Image(
                painter = painterResource(id = R.drawable.logo3),
                contentDescription = "Logo",
                modifier = Modifier.height(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Bienvenido a la PokeApi\nInicia sesión o regístrate",
                modifier = Modifier.padding(8.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(onClick = onLoginClick) {
                Text("Iniciar sesión")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "¿No tienes cuenta? Regístrate abajo.", color = Color.Gray)

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onRegistroClick) {
                Text("Registrarse")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewInicioScreen() {
    InicioScreen()
}

