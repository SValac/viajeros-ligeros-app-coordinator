package com.viajerosligeros.app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viajerosligeros.app.data.DataRepository
import com.viajerosligeros.app.data.model.Travel
import com.viajerosligeros.app.ui.main.TravelsScreenUiState.Success
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TravelsScreenViewModel(dataRepository: DataRepository) : ViewModel() {
  val uiState: StateFlow<TravelsScreenUiState> =
    dataRepository.travels
      .map<List<Travel>, TravelsScreenUiState>(::Success)
      .catch { emit(TravelsScreenUiState.Error(it)) }
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TravelsScreenUiState.Loading)
}

sealed interface TravelsScreenUiState {
  object Loading : TravelsScreenUiState

  data class Error(val throwable: Throwable) : TravelsScreenUiState

  data class Success(val data: List<Travel>) : TravelsScreenUiState
}
