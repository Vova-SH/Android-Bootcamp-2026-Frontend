package ru.sicampus.bootcamp2026.data.network.source

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException
import java.net.URL

class ImageLoaderViewModel : ViewModel() {
    val userAvatar = mutableStateOf<Bitmap?>(null)
    var isLoading = mutableStateOf(false)

    fun loadImage(url: String?) {
        if (url.isNullOrBlank()) return

        viewModelScope.launch {
            isLoading.value = true
            userAvatar.value = imageLoader(url)
            isLoading.value = false
        }
    }

    private suspend fun imageLoader(url: String?): Bitmap? = withContext(Dispatchers.IO) {
        if (url.isNullOrBlank()) return@withContext null

        try {
            return@withContext BitmapFactory.decodeStream(URL(url).openStream())
        } catch (e: IOException) {
            Log.w("imageLoader", "Failed to load image: ${e.message}")
            return@withContext null
        } catch (e: Exception) {
            Log.w("imageLoader", "Unexpected error: ${e.message}")
            return@withContext null
        }
    }
}