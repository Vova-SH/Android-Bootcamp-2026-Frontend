package ru.sicampus.bootcamp2026.domain.service

import android.graphics.Bitmap
import ru.sicampus.bootcamp2026.domain.util.Result

/**
 * Интерфейс для загрузки изображений из URL
 */
interface ImageLoaderService {
    suspend fun loadImage(url: String): Result<Bitmap>
    suspend fun validateImageUrl(url: String): Result<Boolean>
}

