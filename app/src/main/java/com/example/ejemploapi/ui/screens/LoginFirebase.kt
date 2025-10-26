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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ejemploapi.R
import com.example.ejemploapi.ui.viewModels.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit, // Se llama cuando el login es exitoso y el email está verificado
    viewModel: LoginViewModel = LoginViewModel() // ViewModel que gestiona la lógica de login
) {
    val context = LocalContext.current

    // Variables para guardar lo que escribe el usuario
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Diseño de la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Título
        Text(
            text = "Inicio de Sesión",
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(8.dp)) // Espacio entre campos

        // Imagen del logo
        Image(
            painter = painterResource(id = R.drawable.logo3),
            contentDescription = "Logo",
            modifier = Modifier.height(200.dp)
        )

        Spacer(modifier = Modifier.height(8.dp)) // Espacio entre campos

        // Campo de texto para el email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de texto para la contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation() // Oculta los caracteres
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para iniciar sesión
        Button(onClick = {
            // Llamamos al ViewModel para hacer login con email y contraseña
            viewModel.login(email, password) { success, message ->
                if (success) {
                    // Si el login fue exitoso, comprobamos si el email está verificado
                    val user = FirebaseAuth.getInstance().currentUser

                    if (user != null && user.isEmailVerified) {
                        // Si el email está verificado, dejamos entrar
                        onLoginSuccess()
                    } else {
                        // Si no está verificado, mostramos mensaje y cerramos sesión
                        Toast.makeText(
                            context,
                            "Verifica tu email antes de iniciar sesión",
                            Toast.LENGTH_LONG
                        ).show()

                        FirebaseAuth.getInstance().signOut()
                    }
                } else {
                    // Si el login falló, mostramos el mensaje de error
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        }) {
            Text("Iniciar sesión")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(onLoginSuccess = {})
}