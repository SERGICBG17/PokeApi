package com.example.ejemploapi.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    suspend fun register(email: String, password: String): Result<FirebaseUser> {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        if (result.user != null) {
            return Result.success(result.user!!)
        } else {
            return Result.failure(Exception("Error al crear usuario"))
        }
    }

    suspend fun login(email: String, password: String): Result<FirebaseUser> {
        val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        if (result.user != null) {
            return Result.success(result.user!!)
        } else {
            return Result.failure(Exception("Error al iniciar sesión"))
        }
    }

    suspend fun updateEmail(email: String): Result<FirebaseUser> {
        val user = firebaseAuth.currentUser
        if (user != null) {
            user.verifyBeforeUpdateEmail(email).await()
            return Result.success(user)
        } else {
            return Result.failure(Exception("Usuario no autenticado"))
        }
    }

    suspend fun updatePassword(newPassword: String): Result<Unit> {
        val user = firebaseAuth.currentUser ?: return Result.failure(Exception("Usuario no autenticado"))
        return try {
            user.updatePassword(newPassword).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    fun isUserLogin(): Boolean {
        return firebaseAuth.currentUser != null
    }
}
