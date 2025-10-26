package com.example.ejemploapi.ui.viewModels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class DatosViewModel : ViewModel() {

    // Obtenemos una instancia de FirebaseAuth para acceder al usuario actual
    private val firebaseAuth = FirebaseAuth.getInstance()

    // Devuelve el usuario que está logueado en este momento
    fun getUsuarioActual(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    // Intenta actualizar el email del usuario
    // Usa una función que recibe el nuevo email y un callback para saber si funcionó
    fun actualizarEmail(nuevoEmail: String, onResult: (Boolean, String?) -> Unit) {
        val user = firebaseAuth.currentUser

        // Si hay usuario logueado, intentamos actualizar el email
        if (user != null) {
            user.updateEmail(nuevoEmail)
                .addOnCompleteListener { task ->
                    // Si fue exitoso, devolvemos true
                    if (task.isSuccessful) {
                        onResult(true, null)
                    } else {
                        // Si falló, devolvemos false y un mensaje de error
                        onResult(false, "Error al actualizar el email")
                    }
                }
        } else {
            // Si no hay usuario logueado, devolvemos error
            onResult(false, "Usuario no autenticado")
        }
    }

    // Cierra la sesión del usuario actual
    fun cerrarSesion() {
        firebaseAuth.signOut()
    }
}