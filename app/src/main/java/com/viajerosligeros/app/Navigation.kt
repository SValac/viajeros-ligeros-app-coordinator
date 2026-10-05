package com.viajerosligeros.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.viajerosligeros.app.data.SupabaseAuthRepository
import com.viajerosligeros.app.ui.login.LoginScreen
import com.viajerosligeros.app.ui.main.TravelsScreen


@Composable
fun MainNavigation() {
    val sessionViewModel: SessionViewModel = viewModel { SessionViewModel(authRepository = SupabaseAuthRepository()) }
    val authState by sessionViewModel.authState.collectAsStateWithLifecycle()
    when (authState) {
        AuthState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                CircularProgressIndicator()
            }
        }
        AuthState.LoggedIn -> AppNavigation(sessionViewModel::signOut)
        AuthState.LoggedOut -> LoginScreen(modifier = Modifier.safeDrawingPadding().padding(16.dp))
    }
}

@Composable
private fun AppNavigation(
    onSignOut: () -> Unit
) {
    val backStack = rememberNavBackStack(Main)

    Column(Modifier.safeDrawingPadding()) {
        TextButton(onClick = onSignOut, modifier = Modifier.align(Alignment.End), ) {
            Text("Cerrar sesion")
        }
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider =
                entryProvider {
                    entry<Main> {
                        TravelsScreen(onItemClick = { navKey -> backStack.add(navKey) }, modifier = Modifier
                            .safeDrawingPadding()
                            .padding(16.dp))
                    }
                },
        )
    }

}
