package com.teto.planner.presentation.common

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.openTelegramChat(username: String) {
    if (username.isBlank()) return

    val cleanUsername = username.removePrefix("@")
    val appUri = Uri.parse("tg://resolve?domain=$cleanUsername")
    val webUri = Uri.parse("https://t.me/$cleanUsername")

    try {
        val intent = Intent(Intent.ACTION_VIEW, appUri)
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        val intent = Intent(Intent.ACTION_VIEW, webUri)
        startActivity(intent)
    }
}