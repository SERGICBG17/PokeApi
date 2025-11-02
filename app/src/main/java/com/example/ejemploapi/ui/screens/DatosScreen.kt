package com.example.ejemploapi.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ejemploapi.ui.viewModels.PerfilViewModel

/**
 * Pantalla de perfil del usuario.
 * - Muestra el email actual
 * - Permite cambiar el email
 * - Permite cerrar sesión
 */
@Composable
fun DatosScreen(
    onLogout: () -> Unit,
    viewModel: PerfilViewModel = viewModel()
) {
    val context = LocalContext.current

    // Observamos los estados del ViewModel
    val emailActual by viewModel.emailActual.observeAsState()
    val mensaje by viewModel.mensaje.observeAsState()
    val actualizando by viewModel.actualizando.observeAsState(false)

    var nuevoEmail by remember { mutableStateOf(TextFieldValue("")) }

    // Actualizamos el ViewModel cuando el usuario escribe
    LaunchedEffect(nuevoEmail.text) {
        viewModel.onNuevoEmailChanged(nuevoEmail.text)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Perfil", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Email actual:")
        Text(emailActual ?: "Sin email", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = nuevoEmail,
            onValueChange = { nuevoEmail = it },
            label = { Text("Nuevo email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.actualizarEmail() },
            enabled = !actualizando,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (actualizando) CircularProgressIndicator(modifier = Modifier.size(20.dp))
            else Text("Actualizar email")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                viewModel.cerrarSesion()
                Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                onLogout()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión")
        }

        Spacer(modifier = Modifier.height(16.dp))

        mensaje?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}