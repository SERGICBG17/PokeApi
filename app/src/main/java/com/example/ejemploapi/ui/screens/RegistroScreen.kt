package com.example.ejemploapi.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ejemploapi.R
import com.example.ejemploapi.ui.viewModels.RegistroViewModel

@Composable
fun RegistroScreen(
    onRegistroSuccess: () -> Unit,
    onBack: () -> Unit,
    viewModel: RegistroViewModel = viewModel()
) {
    val context = LocalContext.current

    val isLoading by viewModel.isRegistrer.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState(false)
    val isRegistroOk by viewModel.isRegistrerOk.observeAsState(false)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(email) { viewModel.onEmailChanged(email) }
    LaunchedEffect(password) { viewModel.onPasswordChanged(password) }

    LaunchedEffect(isRegistroOk) {
        if (isRegistroOk) {
            Toast.makeText(context, "Registro exitoso. Verifica tu email.", Toast.LENGTH_LONG).show()
            onRegistroSuccess()
        }
    }

    LaunchedEffect(errorMessage) {
        if (errorMessage) {
            Toast.makeText(context, "Error al registrar usuario", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        // Imagen del logo
        Image(
            painter = painterResource(id = R.drawable.logo3),
            contentDescription = "Logo",
            modifier = Modifier.height(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Registro", style = MaterialTheme.typography.titleLarge)

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
            onClick = { viewModel.onRegistrer() },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) CircularProgressIndicator(modifier = Modifier.size(20.dp))
            else Text("Registrarse")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onBack ,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }

    }
}
