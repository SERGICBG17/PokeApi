package com.example.ejemploapi.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ejemploapi.ui.viewModels.LoginViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val context = LocalContext.current

    // Observamos los estados del ViewModel
    val isLoading by viewModel.isLoading.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState(false)
    val isLoginOk by viewModel.isLoadiginOk.observeAsState(false)

    // Campos de texto locales
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Actualizamos el ViewModel cuando el usuario escribe
    LaunchedEffect(email) { viewModel.onEmailChanged(email) }
    LaunchedEffect(password) { viewModel.onPasswordChanged(password) }

    // Si el login fue exitoso, navegamos
    LaunchedEffect(isLoginOk) {
        if (isLoginOk) {
            onLoginSuccess()
        }
    }

    // Si hay error, mostramos un Toast
    LaunchedEffect(errorMessage) {
        if (errorMessage) {
            Toast.makeText(context, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Inicio de Sesión", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))

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
            onClick = { viewModel.onLogin() },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) CircularProgressIndicator(modifier = Modifier.size(20.dp))
            else Text("Iniciar sesión")
        }
    }
}
