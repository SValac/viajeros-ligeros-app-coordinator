package com.viajerosligeros.app.data.remote

import com.viajerosligeros.app.BuildConfig
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

val supabase = createSupabaseClient(supabaseUrl = BuildConfig.SUPABASE_URL ,supabaseKey = BuildConfig.SUPABASE_KEY) {
    install(Auth)
    install(Postgrest)
}