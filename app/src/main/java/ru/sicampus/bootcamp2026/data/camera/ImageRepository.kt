package ru.sicampus.bootcamp2026.data.camera

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.UUID

class ImageRepository {

    suspend fun uploadImageUri(context: Context, imageUri:Uri): String =
        withContext(Dispatchers.IO){
            val fileName = "${UUID.randomUUID()}.jpg"
            val bytes = context.contentResolver.openInputStream(imageUri)
                ?.readBytes()?: error("Cannot read image from uri")
            SupabaseProvider.client.storage.from("images").upload(
                path = fileName,
                data = bytes,
                upsert = false
            )
            SupabaseProvider.client.storage.from("images").publicUrl(fileName)
        }

    suspend fun uploadImage(bitmap: Bitmap): String =
        withContext(Dispatchers.IO){
            val fileName = "${UUID.randomUUID()}.jpg"
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val bytes = stream.toByteArray()
            SupabaseProvider.client.storage.from("images").upload(
                path = fileName,
                data = bytes,
                upsert = false
            )
            SupabaseProvider.client.storage.from("images").publicUrl(fileName)
        }

}