package com.viajerosligeros.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viajerosligeros.app.data.AuthRepository
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SessionViewModel(private val authRepository: AuthRepository) : ViewModel() {
    val authState: StateFlow<AuthState> =
        authRepository.sessionStatus
            .map { status ->
                when (status) {
                    is SessionStatus.Initializing -> AuthState.Loading
                    is SessionStatus.Authenticated -> if (authRepository.isCoordinator()) AuthState.LoggedIn else AuthState.LoggedOut
                    is SessionStatus.NotAuthenticated -> AuthState.LoggedOut
                    is SessionStatus.RefreshFailure -> AuthState.LoggedIn
                }
            }
            .stateIn (
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                AuthState.Loading
            )

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
        }
    }
}


sealed interface AuthState {
    data object Loading : AuthState
    data object LoggedOut : AuthState
    data object LoggedIn : AuthState
}