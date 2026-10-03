package com.viajerosligeros.app.ui.main

import com.viajerosligeros.app.data.DataRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test

class MainScreenViewModelTest {
  @Test
  fun uiState_initiallyLoading() = runTest {
    val viewModel = TravelsScreenViewModel(FakeMyModelRepository())
    assertEquals(viewModel.uiState.first(), TravelsScreenUiState.Loading)
  }

  @Test
  fun uiState_onItemSaved_isDisplayed() = runTest {
    val viewModel = TravelsScreenViewModel(FakeMyModelRepository())
    assertEquals(viewModel.uiState.first(), TravelsScreenUiState.Loading)
  }
}

private class FakeMyModelRepository : DataRepository {
  override val data: Flow<List<String>> = flow { emit(listOf("Sample")) }
}
