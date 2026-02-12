package ru.sicampus.bootcamp2026.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage


object SupabaseProvider {
    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://fcaqgphpgqrswtafjdbc.supabase.co",
        supabaseKey = "sb_publishable_V0J9mcGx2o5dnD9fxRRIHg_CqtzD1mh"
    ){
        install(Storage)
    }
}