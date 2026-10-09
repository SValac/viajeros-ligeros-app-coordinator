package com.viajerosligeros.app.data

import com.viajerosligeros.app.data.model.Travel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface DataRepository {
  val travels: Flow<List<Travel>>

  fun travel(id: String): Flow<Travel>
}

class DefaultDataRepository : DataRepository {

  override val travels: Flow<List<Travel>> = flow {
    delay(1500)
    emit(sampleTravels)
  }

  override fun travel(id: String): Flow<Travel> {
    return flow { emit( sampleTravels.first { travel -> travel.id == id } ) };
  }


}
