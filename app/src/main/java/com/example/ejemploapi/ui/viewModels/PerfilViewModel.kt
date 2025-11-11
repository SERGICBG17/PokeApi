package com.example.ejemploapi.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ejemploapi.data.repository.AuthRepository
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * ViewModel para la pantalla de perfil del usuario.
 * - Muestra el email actual
 * - Permite actualizar el email
 * - Permite cerrar sesión
 */
class PerfilViewModel : ViewModel() {

    private val authRepository = AuthRepository()
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

    private val _isLoading = MutableLiveData(false)
    val isLoading : LiveData<Boolean> = _isLoading

    private val _isLoadiginOk = MutableLiveData(false)
    val isLoadiginOk : LiveData<Boolean> = _isLoadiginOk

    fun onNuevoEmailChanged(email: String) {
        _nuevoEmail.value = email
    }

    fun actualizarEmail(newEmail: String) {
        if (!newEmail.contains("@")) {
            _mensaje.value = "Email no válido"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            val res = authRepository.updateEmail(newEmail)
            res.onSuccess {
                _mensaje.value = "Correo actualizado correctamente. Revisa el email para verificarlo."
            }
            res.onFailure {
                _mensaje.value = "Error al actualizar correo: ${it.message}"
            }
            _isLoading.value = false
        }
    }

    fun updatePassword(newPassword: String) {
        if (newPassword.isNotEmpty() && newPassword.length < 8) {
            _mensaje.value = "La contraseña debe tener al menos 8 caracteres"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            val res = authRepository.updatePassword(newPassword)
            res.onSuccess {
                _mensaje.value = "Contraseña actualizada correctamente"
            }
            res.onFailure {
                _mensaje.value = "Error al actualizar contraseña: ${it.message}"
            }
            _isLoading.value = false
        }
    }

    fun cerrarSesion() {
        firebaseAuth.signOut()
    }
}
