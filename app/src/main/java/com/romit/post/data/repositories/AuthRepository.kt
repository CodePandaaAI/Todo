package com.romit.post.data.repositories

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

interface AuthRepository {
    suspend fun signIn(userEmail: String, userPassword: String): AuthResult
    suspend fun signUp(userEmail: String, userPassword: String): AuthResult
}

class AuthRepositoryImpl : AuthRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()

    override suspend fun signIn(userEmail: String, userPassword: String): AuthResult {
        return firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).await()
    }

    override suspend fun signUp(userEmail: String, userPassword: String): AuthResult {
        return firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).await()
    }
}