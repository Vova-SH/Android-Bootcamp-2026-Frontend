package ru.sicampus.bootcamp2026.data.camera

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.sicampus.bootcamp2026.data.auth.NetworkClient
import java.io.ByteArrayOutputStream
import java.util.UUID

class ImageRepository {

    private val api = NetworkClient.createAppApi()

    suspend fun uploadImageUri(context: Context, uri: Uri): String {
        val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            ?: error("Не удалось прочитать файл")

        return uploadBytes(bytes)
    }

    suspend fun uploadImage(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val bytes = stream.toByteArray()

        return uploadBytes(bytes)
    }

    private suspend fun uploadBytes(bytes: ByteArray): String {
        val requestBody = bytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("image", "avatar_${UUID.randomUUID()}.jpg", requestBody)

        return api.uploadImage(part).url
    }
}