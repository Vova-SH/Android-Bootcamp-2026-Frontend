package ru.sicampus.bootcamp2026.data.camera

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.storage.Storage

object SupabaseProvider {

    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://sgtlzivyrjbthtqxtint.supabase.co",
        supabaseKey = "sb_publishable_Y59QvGtMuejoi5PQJx1EOg_6Wuicg0l"
    ){
        install(Auth)
        install(Storage)
    }
}