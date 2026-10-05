package com.viajerosligeros.app.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viajerosligeros.app.data.AuthRepository
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.exceptions.HttpRequestException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository): ViewModel() {

    private val _uiState = MutableStateFlow(LoginScreenUiState())
    val uiState : StateFlow<LoginScreenUiState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, error = null) }
    }
    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, error = null) }
    }

    fun signIn() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                authRepository.signIn(email = uiState.value.email, password = uiState.value.password)
                if (!authRepository.isCoordinator()) {
                    authRepository.signOut()
                    _uiState.update { it.copy(error = "Esta cuenta no tiene acceso de coordinador") }
                }
            } catch (e: AuthRestException) {
                _uiState.update {
                        it.copy(error = "Credenciales incorrectas")
                }
            } catch (e: HttpRequestException) {
                _uiState.update { it.copy(error = "Error de conexion") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}

data class LoginScreenUiState(
    val email: String = "",
    val password: String = "",
    val error: String? = null,
    val isLoading: Boolean = false
) {
    val canSubmit: Boolean get() = !isLoading && email.isNotEmpty() && password.isNotEmpty()
}