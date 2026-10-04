package com.viajerosligeros.app.data

import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow


interface AuthRepository {
    val sessionStatus: Flow<SessionStatus>
    suspend fun signIn(email: String, password: String)
    suspend fun signOut()
    fun isCoordinator(): Boolean
}