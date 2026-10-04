package com.viajerosligeros.app.data

import com.viajerosligeros.app.data.model.Travel
import com.viajerosligeros.app.data.remote.supabase
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class SupabaseTravelRepository: DataRepository {
    override val travels: Flow<List<Travel>> = flow {
     emit(supabase
         .from("travels")
         .select(Columns.list("id", "label", "start_date", "status"))
         .decodeList<Travel>())
    }
}