package com.example.ejemploapi

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ejemploapi.ui.screens.LoginFirebase
import com.example.ejemploapi.ui.screens.Registro
import com.example.ejemploapi.ui.theme.EjemploApiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EjemploApiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {

    var contexto = LocalContext.current
    var intentRegistro = Intent(contexto, Registro::class.java)
    var intentInicio = Intent(contexto, LoginFirebase::class.java)

    // Contenedor visual con fondo gris claro

    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()           // Ocupa toda la pantalla
                .padding(32.dp),         // Margen interno de 32dp
            horizontalAlignment = Alignment.CenterHorizontally, // Centra los elementos horizontalmente
            verticalArrangement = Arrangement.Top               // Alinea los elementos desde arriba
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            Image(
                painter = painterResource(id = R.drawable.logo3),
                contentDescription = "Logo",
                modifier = Modifier.height(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Bienvenido a la PokeApi" +
                        "\n   inicia sesión o registrate",
                modifier = modifier
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    contexto.startActivity(intentInicio)
                }
            ) {
                Text(text = "Iniciar sesión")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "¿No tienes cuenta? Regístrate abajo.",
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))


            Button(
                onClick = {
                    contexto.startActivity(intentRegistro)
                }
            ) {
                Text(text = "Registrarse")
            }

        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    EjemploApiTheme {
//    }
//}