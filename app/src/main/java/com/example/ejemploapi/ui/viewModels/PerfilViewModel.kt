package com.example.ejemploapi.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * ViewModel para la pantalla de perfil del usuario.
 * - Muestra el email actual
 * - Permite actualizar el email
 * - Permite cerrar sesión
 */
class PerfilViewModel : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    private val _emailActual = MutableLiveData<String?>()
    val emailActual: LiveData<String?> = _emailActual

    private val _nuevoEmail = MutableLiveData<String?>()
    val nuevoEmail: LiveData<String?> = _nuevoEmail

    private val _mensaje = MutableLiveData<String?>()
    val mensaje: LiveData<String?> = _mensaje

    private val _actualizando = MutableLiveData(false)
    val actualizando: LiveData<Boolean> = _actualizando

    init {
        _emailActual.value = firebaseAuth.currentUser?.email
    }

    fun onNuevoEmailChanged(email: String) {
        _nuevoEmail.value = email
    }

    fun actualizarEmail() {
        val user = firebaseAuth.currentUser
        val nuevo = _nuevoEmail.value ?: ""

        if (user == null) {
            _mensaje.value = "Usuario no autenticado"
            return
        }

        if (nuevo.isBlank()) {
            _mensaje.value = "Introduce un email válido"
            return
        }

        _actualizando.value = true

        user.updateEmail(nuevo)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _emailActual.value = nuevo
                    _mensaje.value = "Email actualizado correctamente"
                } else {
                    _mensaje.value = "Error al actualizar el email"
                }
                _actualizando.value = false
            }
    }

    fun cerrarSesion() {
        firebaseAuth.signOut()
    }
}
