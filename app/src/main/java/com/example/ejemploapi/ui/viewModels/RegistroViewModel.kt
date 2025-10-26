package com.example.ejemploapi.ui.viewModels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegistroViewModel : ViewModel() {

    // Creamos una instancia de FirebaseAuth para acceder a la autenticación
    private val firebaseAuth = FirebaseAuth.getInstance()

    // Función que intenta registrar un nuevo usuario con email y contraseña
    fun registrar(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        // Comprobamos que el usuario haya escrito ambos campos
        if (email.isBlank() || password.isBlank()) {
            onResult(false, "Rellena todos los campos") // Si falta algo, mostramos mensaje
            return
        }

        // Usamos Firebase para crear el usuario
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Si se creó correctamente, obtenemos el usuario actual
                    val user = firebaseAuth.currentUser
                    if (user != null) {
                        // Le enviamos un email de verificación
                        verificarEmail(user)
                    }
                    // Indicamos que el registro fue exitoso
                    onResult(true, null)
                } else {
                    // Si falló (por ejemplo, email ya usado), mostramos mensaje de error
                    onResult(false, "Error al registrar usuario")
                }
            }
    }

    // Función que envía un email de verificación al usuario
    private fun verificarEmail(user: FirebaseUser) {
        user.sendEmailVerification() // Firebase se encarga de enviar el correo
    }
}

