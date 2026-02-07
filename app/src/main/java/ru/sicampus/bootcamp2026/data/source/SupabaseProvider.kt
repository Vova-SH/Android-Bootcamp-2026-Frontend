package ru.sicampus.bootcamp2026.data.source

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.storage.Storage

object SupabaseProvider {

    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://erglsgdjocumqqxbwcgy.supabase.co",
        supabaseKey = "sb_publishable_IOOJIVx7uHEqNLpy6bynmw_ysZxVtuC"
    ) {
        install(Storage.Companion)
    }
}