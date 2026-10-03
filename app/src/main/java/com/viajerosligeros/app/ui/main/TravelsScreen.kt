package com.viajerosligeros.app.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import com.viajerosligeros.app.data.DefaultDataRepository
import com.viajerosligeros.app.data.model.Travel
import com.viajerosligeros.app.data.sampleTravels
import com.viajerosligeros.app.theme.ViajerosLigerosTheme

@Composable
fun TravelsScreen(
  onItemClick: (NavKey) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: TravelsScreenViewModel = viewModel { TravelsScreenViewModel(DefaultDataRepository()) },
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  when (val s = state) {
    TravelsScreenUiState.Loading -> {
      Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        CircularProgressIndicator()
      }
    }
    is TravelsScreenUiState.Success -> {
      TravelsScreen(data = s.data, modifier = modifier)
    }
    is TravelsScreenUiState.Error -> {
      Text("Error loading data: ${s.throwable.message}", modifier)
    }
  }
}

@Composable
internal fun TravelsScreen(data: List<Travel>, modifier: Modifier = Modifier) {
  LazyColumn(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
    items(data, key = {it.id}) { travel ->
      Card(modifier = Modifier.fillParentMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
          Text(travel.name, style = MaterialTheme.typography.titleMedium)
          Text(travel.startDate)
          Text(travel.status)
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun TravelsScreenPreview() {
  ViajerosLigerosTheme { TravelsScreen(sampleTravels) }
}

@Preview(showBackground = true, widthDp = 340)
@Composable
fun TravelsScreenPortraitPreview() {
  ViajerosLigerosTheme { TravelsScreen(sampleTravels) }
}
