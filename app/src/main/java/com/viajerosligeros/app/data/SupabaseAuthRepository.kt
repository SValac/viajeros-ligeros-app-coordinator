package com.viajerosligeros.app.data

import com.viajerosligeros.app.data.remote.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.jsonPrimitive

class SupabaseAuthRepository: AuthRepository {
    override val sessionStatus: Flow<SessionStatus> = supabase.auth.sessionStatus

    override suspend fun signIn(email: String, password: String) {
        supabase.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    override suspend fun signOut() {
        supabase.auth.signOut()
    }

    override fun isCoordinator(): Boolean {
        return supabase.auth.currentUserOrNull()?.appMetadata?.get("role")?.jsonPrimitive?.content == "coordinator"
    }
}