package com.example.ejemploapi.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ejemploapi.ui.viewModels.DatosViewModel

@Composable
fun DatosScreen(
    onLogout: () -> Unit, // Se llama cuando el usuario cierra sesión
    viewModel: DatosViewModel = DatosViewModel() // ViewModel que gestiona los datos del usuario
) {
    val context = LocalContext.current // Necesario para mostrar mensajes (Toast)
    val usuario = viewModel.getUsuarioActual() // Obtenemos el usuario actual
    var nuevoEmail by remember { mutableStateOf(usuario?.email ?: "") } // Email editable en pantalla

    // Todo lo que se muestra en pantalla
    Column(
        modifier = Modifier
            .fillMaxSize() // Ocupa toda la pantalla
            .padding(16.dp), // Margen interno
        verticalArrangement = Arrangement.Center, // Centrado vertical
        horizontalAlignment = Alignment.CenterHorizontally // Centrado horizontal
    ) {
        // Título de la pantalla
        Text(text = "Datos del usuario", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp)) // Espacio entre elementos

        // Campo para editar el email
        OutlinedTextField(
            value = nuevoEmail, // Lo que se muestra en el campo
            onValueChange = { nuevoEmail = it }, // Se actualiza cuando el usuario escribe
            label = { Text("Email") }, // Etiqueta del campo
            modifier = Modifier.fillMaxWidth() // Ocupa todo el ancho
        )

        Spacer(modifier = Modifier.height(16.dp)) // Espacio entre campo y botón

        // Botón para guardar el nuevo email
        Button(
            onClick = {
                // Llamamos al ViewModel para actualizar el email
                viewModel.actualizarEmail(nuevoEmail) { success, message ->
                    // Mostramos un mensaje corto en pantalla según si funcionó o no
                    val texto = if (success) "Email actualizado" else message
                    Toast.makeText(context, texto, Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Actualizar email") // Texto del botón
        }

        Spacer(modifier = Modifier.height(32.dp)) // Espacio antes del botón de cerrar sesión

        // Botón para cerrar sesión
        Button(
            onClick = {
                viewModel.cerrarSesion() // Cerramos sesión desde el ViewModel
                onLogout() // Ejecutamos la función que nos pasaron (por ejemplo, navegar al login)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión") // Texto del botón
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDatosScreen() {
    DatosScreen(onLogout = {})
}

