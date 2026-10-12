package com.viajerosligeros.app.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viajerosligeros.app.data.DataRepository
import com.viajerosligeros.app.data.model.Travel
import com.viajerosligeros.app.ui.details.TravelDetailsUiState.Success
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TravelDetailsViewModel(dataRepository: DataRepository, id: String): ViewModel() {
    val uiState : StateFlow<TravelDetailsUiState> =
        dataRepository.travel(id)
            .map<Travel, TravelDetailsUiState>(::Success)
            .catch { emit(TravelDetailsUiState.Error(it)) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TravelDetailsUiState.Loading)
}


sealed interface TravelDetailsUiState {
    object Loading : TravelDetailsUiState
    data class Error(val throwable: Throwable) : TravelDetailsUiState
    data class Success(val travel: Travel) : TravelDetailsUiState
}