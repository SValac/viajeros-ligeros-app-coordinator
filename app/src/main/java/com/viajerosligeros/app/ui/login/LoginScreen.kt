package com.viajerosligeros.app.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.viajerosligeros.app.data.SupabaseAuthRepository
import com.viajerosligeros.app.theme.ViajerosLigerosTheme

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel { LoginViewModel(SupabaseAuthRepository()) }
){
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LoginScreen(state = state, onEmailChange = viewModel::onEmailChange, onPasswordChange = viewModel::onPasswordChange, onSubmit = viewModel::signIn, modifier = modifier)
}

@Composable
internal fun LoginScreen(state: LoginScreenUiState, onEmailChange: (String) -> Unit, onPasswordChange: (String) ->Unit, onSubmit: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Viajeros Ligeros", style = MaterialTheme.typography.headlineMedium)
        TextField(
            value = state.email,
            onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Email),
            enabled = !state.isLoading,
            singleLine = true,
            label = {
                Text("Correo")
            },
        )
        TextField(
            value = state.password,
            onValueChange = onPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password),
            keyboardActions = KeyboardActions(onDone = { if (state.canSubmit) onSubmit() }),
            singleLine = true,
            label = {
                Text("Contraseña")
            },
            enabled = !state.isLoading)
        Button(onClick = { onSubmit() }, enabled = state.canSubmit, modifier = Modifier.fillMaxWidth()) {
            if ( state.isLoading ) {
                CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
                Text("Entrar")
            }
        }
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    ViajerosLigerosTheme {
        LoginScreen(
            state = LoginScreenUiState(),
            onEmailChange = {},
            onPasswordChange = {},
            onSubmit = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreviewLoading() {
    ViajerosLigerosTheme {
        LoginScreen(
            state = LoginScreenUiState(
                email = "sofia@dev-local.com",
                password = "password123",
                isLoading = true,
            ),
            onEmailChange = {},
            onPasswordChange = {},
            onSubmit = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreviewError() {
    ViajerosLigerosTheme {
        LoginScreen(
            state = LoginScreenUiState(
                email = "sofia@dev-local.com",
                password = "password123",
                error = "Credenciales incorrectas"
            ),
            onEmailChange = {},
            onPasswordChange = {},
            onSubmit = {}
        )
    }
}
