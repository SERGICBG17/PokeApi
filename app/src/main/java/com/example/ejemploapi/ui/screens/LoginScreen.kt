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
import com.example.ejemploapi.ui.viewModels.LoginViewModel

/**
 * Pantalla de inicio de sesión.
 * - Muestra campos de email y contraseña
 * - Llama al ViewModel para hacer login
 * - Reacciona a estados de carga, error y éxito
 */
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val context = LocalContext.current

    val cargando by viewModel.cargando.collectAsState()
    val error by viewModel.error.collectAsState()
    val loginExitoso by viewModel.loginExitoso.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Si el login fue exitoso, navegamos
    LaunchedEffect(loginExitoso) {
        if (loginExitoso) {
            onLoginSuccess()
        }
    }

    // Si hay error, mostramos un Toast
    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.limpiarError()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Inicio de Sesión", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))

        // Imagen del logo
        Image(
            painter = painterResource(id = R.drawable.logo3),
            contentDescription = "Logo",
            modifier = Modifier.height(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.login(email, password) },
            enabled = !cargando,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (cargando) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
            } else {
                Text("Iniciar sesión")
            }
        }
    }
}