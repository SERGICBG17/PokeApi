package com.example.ejemploapi.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.ejemploapi.R

@Composable
fun RegistroScreen(
    onRegistroSuccess: () -> Unit // Esta función se llama cuando el registro fue exitoso
) {
    // Obtenemos el contexto actual, necesario para Firebase y Toast
    val context = LocalContext.current

    // Convertimos el contexto en una actividad, si es posible
    val activity = context as? ComponentActivity

    // Obtenemos la instancia de FirebaseAuth
    val firebaseAuth = remember { FirebaseAuth.getInstance() }

    // Variables para guardar el email y la contraseña que escribe el usuario
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Estructura visual de la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // Espaciado interno
        verticalArrangement = Arrangement.Center, // Centramos verticalmente
        horizontalAlignment = Alignment.CenterHorizontally // Centramos horizontalmente
    ) {

        // Título de la pantalla
        Text(
            text = "Registro",
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(8.dp)) // Espacio entre elementos

        // Imagen del logo
        Image(
            painter = painterResource(id = R.drawable.logo3),
            contentDescription = "Logo",
            modifier = Modifier.height(200.dp)
        )

        Spacer(modifier = Modifier.height(8.dp)) // Espacio entre elementos

        // Campo de texto para el email
        OutlinedTextField(
            value = email,
            onValueChange = fun(nuevoEmail: String) {
                email = nuevoEmail
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp)) // Espacio entre campos

        // Campo de texto para la contraseña
        OutlinedTextField(
            value = password,
            onValueChange = fun(nuevaPass: String) {
                password = nuevaPass
            },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp)) // Espacio antes del botón

        // Botón para registrar
        Button(
            onClick = fun() {
                // Si tenemos una actividad válida, llamamos a la función de registro
                if (activity != null) {
                    registro(email, password, firebaseAuth, activity, onRegistroSuccess)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }
    }
}

// Esta función registra al usuario en Firebase y verifica el email
fun registro(
    email: String,
    pass: String,
    firebaseAuth: FirebaseAuth,
    activity: ComponentActivity,
    onRegistroSuccess: () -> Unit // Esta función se llama para navegar al login
) {
    // Comprobamos que los campos no estén vacíos
    if (email.isNotEmpty() && pass.isNotEmpty()) {
        // Registramos el usuario con email y contraseña
        firebaseAuth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Obtenemos el usuario recién creado
                    val user: FirebaseUser? = firebaseAuth.currentUser

                    if (user != null) {
                        // Enviamos email de verificación
                        user.sendEmailVerification()
                            .addOnCompleteListener(activity) { verifyTask ->
                                if (verifyTask.isSuccessful) {
                                    // Si el email se envió correctamente, avisamos al usuario
                                    Toast.makeText(
                                        activity,
                                        "Email de verificación enviado. Revisa tu bandeja.",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    // Cerramos sesión para evitar que el usuario acceda sin verificar
                                    firebaseAuth.signOut()

                                    // Navegamos a la pantalla de inicio de sesión
                                    onRegistroSuccess()
                                } else {
                                    // Si falló el envío del email, mostramos mensaje
                                    Toast.makeText(
                                        activity,
                                        "Error al enviar verificación",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
                } else {
                    // Si hubo error al crear el usuario, mostramos mensaje
                    Toast.makeText(activity, "Error al registrar usuario", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { error ->
                // Si la creación falla, mostramos mensaje con el error
                Toast.makeText(activity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    } else {
        // Si los campos están vacíos, avisamos al usuario
        Toast.makeText(activity, "Inserta los datos correctamente", Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegistroScreen() {
    RegistroScreen(onRegistroSuccess = {})
}