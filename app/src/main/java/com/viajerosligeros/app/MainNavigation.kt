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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.viajerosligeros.app.data.DataRepository
import com.viajerosligeros.app.data.SupabaseAuthRepository
import com.viajerosligeros.app.data.SupabaseTravelRepository
import com.viajerosligeros.app.ui.details.TravelDetailsViewModel
import com.viajerosligeros.app.ui.login.LoginScreen
import com.viajerosligeros.app.ui.main.TravelsScreen


@Composable
fun MainNavigation() {
    val sessionViewModel: SessionViewModel =
        viewModel { SessionViewModel(authRepository = SupabaseAuthRepository()) }
    val authState by sessionViewModel.authState.collectAsStateWithLifecycle()

    if (authState == AuthState.Loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }
    val rootScreen = if (authState == AuthState.LoggedIn) Main else Login

    val backStack = rememberNavBackStack(rootScreen)

    LaunchedEffect(authState) {
        if (backStack.firstOrNull() != rootScreen) {
            backStack.clear()
            backStack.add(rootScreen)
        }
    }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider =
            entryProvider {
                entry<Login> {
                    LoginScreen(
                        modifier = Modifier
                            .safeDrawingPadding()
                            .padding(16.dp)
                    )
                }
                entry<Main> {
                    Column(modifier = Modifier.safeDrawingPadding()) {
                        TextButton(
                            onClick = sessionViewModel::signOut,
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Cerrar sesión")
                        }
                        TravelsScreen(
                            onItemClick = { travelId -> backStack.add(TravelDetails(travelId)) },
                            modifier = Modifier
                                .padding(16.dp),
                        )
                    }
                }
                entry<TravelDetails> { key ->
                    val travelDetailsViewModel = viewModel {
                        TravelDetailsViewModel(
                            dataRepository = SupabaseTravelRepository(),
                            id = key.id
                        )
                    }
                    val travelState by travelDetailsViewModel.uiState.collectAsStateWithLifecycle()
                    Column(Modifier.safeDrawingPadding()) {
                        Text(travelState.toString())
                    }
                }
            },
    )
}