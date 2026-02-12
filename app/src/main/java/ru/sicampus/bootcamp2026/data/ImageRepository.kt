package ru.sicampus.bootcamp2026.data

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import java.io.ByteArrayOutputStream


class ImageRepository(){
    private val _userState = MutableStateFlow<UserDto?>(null)
    val userState: StateFlow<UserDto?> = _userState.asStateFlow()
    suspend fun uploadImageUri(context: Context, imageUri:Uri, userId: Int): String =
        withContext(Dispatchers.IO){

            val fileName = "${userId}_profile.jpg"
            val bytes = context.contentResolver.openInputStream(imageUri)
                ?.readBytes()?: error("Cannot read image from uri")
            SupabaseProvider.client.storage.from("images").upload(
                path = fileName,
                data = bytes,
                upsert = true
            )
            return@withContext SupabaseProvider.client.storage.from("images").publicUrl(fileName)
        }

    suspend fun uploadImage(bitmap: Bitmap, userId: Int): String =
        withContext(Dispatchers.IO){
            val fileName = "${userId}_profile.jpg"
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val bytes = stream.toByteArray()
            SupabaseProvider.client.storage.from("images").upload(
                path = fileName,
                data = bytes,
                upsert = true
            )
            return@withContext SupabaseProvider.client.storage.from("images").publicUrl(fileName)
        }

    suspend fun getProfileImageUrl(userId: Int): String? =
        withContext(Dispatchers.IO) {
            try {
                val fileName = "${userId}_profile.jpg"
                SupabaseProvider.client.storage
                    .from("images")
                    .publicUrl(fileName)
            } catch (e: Exception) {
                return@withContext null
            }
        }


}