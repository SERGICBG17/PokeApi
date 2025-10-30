package com.example.ejemploapi.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ejemploapi.R
import com.example.ejemploapi.ui.viewModels.RegistroViewModel

/**
 * Pantalla de registro de usuario.
 * - Pide email y contraseña
 * - Llama al ViewModel para registrar
 * - Muestra mensajes de error o éxito
 */
@Composable
fun RegistroScreen(
    onRegistroSuccess: () -> Unit,
    viewModel: RegistroViewModel = viewModel()
) {
    val context = LocalContext.current

    // Estados que vienen del ViewModel
    val cargando by viewModel.cargando.collectAsState()
    val error by viewModel.error.collectAsState()
    val registroExitoso by viewModel.registroExitoso.collectAsState()

    // Estados locales de los campos de texto
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Si el registro fue exitoso, mostramos mensaje y navegamos
    LaunchedEffect(registroExitoso) {
        if (registroExitoso) {
            Toast.makeText(
                context,
                "Email de verificación enviado. Revisa tu bandeja.",
                Toast.LENGTH_LONG
            ).show()
            onRegistroSuccess()
        }
    }

    // Si hay error, mostramos un Toast
    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.limpiarError()
        }
    }

    // Diseño de la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // margen interno
        verticalArrangement = Arrangement.Center, // centrado vertical
        horizontalAlignment = Alignment.CenterHorizontally // centrado horizontal
    ) {
        // Título
        Text("Registro", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))

        // Imagen del logo
        Image(
            painter = painterResource(id = R.drawable.logo3),
            contentDescription = "Logo",
            modifier = Modifier.height(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(), // oculta caracteres
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de registro
        Button(
            onClick = { viewModel.registrar(email, password) },
            enabled = !cargando, // deshabilitado mientras carga
            modifier = Modifier.fillMaxWidth()
        ) {
            if (cargando) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
            } else {
                Text("Registrarse")
            }
        }
    }
}