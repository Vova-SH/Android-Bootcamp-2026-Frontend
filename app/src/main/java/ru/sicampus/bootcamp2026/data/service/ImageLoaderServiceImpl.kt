package ru.sicampus.bootcamp2026.data.service

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.domain.service.ImageLoaderService
import ru.sicampus.bootcamp2026.domain.util.Result
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Реализация сервиса для загрузки изображений из URL
 */
@Singleton
class ImageLoaderServiceImpl @Inject constructor() : ImageLoaderService {

    override suspend fun loadImage(url: String): Result<Bitmap> = withContext(Dispatchers.IO) {
        return@withContext try {
            if (url.isBlank()) {
                return@withContext Result.Error(Exception("URL не может быть пустым"))
            }

            // Проверяем формат URL
            if (!isValidUrl(url)) {
                return@withContext Result.Error(Exception("Невалидный формат URL"))
            }

            // Загружаем изображение
            val urlConnection = URL(url).openConnection()
            urlConnection.connectTimeout = 10000 // 10 секунд
            urlConnection.readTimeout = 10000    // 10 секунд

            val inputStream = urlConnection.getInputStream()
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            if (bitmap == null) {
                Result.Error(Exception("Не удалось загрузить изображение"))
            } else {
                Result.Success(bitmap)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun validateImageUrl(url: String): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext try {
            if (url.isBlank()) {
                return@withContext Result.Success(false)
            }

            // Проверяем формат URL
            if (!isValidUrl(url)) {
                return@withContext Result.Success(false)
            }

            // Пытаемся подключиться и получить заголовки
            val urlConnection = URL(url).openConnection()
            urlConnection.connectTimeout = 5000
            urlConnection.readTimeout = 5000
            urlConnection.connect()

            val contentType = urlConnection.contentType ?: ""
            val isImage = contentType.startsWith("image/")

            Result.Success(isImage)
        } catch (e: Exception) {
            Result.Success(false)
        }
    }

    /**
     * Проверка валидности URL
     */
    private fun isValidUrl(url: String): Boolean {
        return try {
            URL(url)
            url.startsWith("http://") || url.startsWith("https://")
        } catch (e: Exception) {
            false
        }
    }
}

