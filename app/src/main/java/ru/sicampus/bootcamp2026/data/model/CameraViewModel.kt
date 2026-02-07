package ru.sicampus.bootcamp2026.data.model

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.http.Url
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.camera.ImageRepository

class CameraViewModal(private val repository: ImageRepository) : ViewModel(){
    // изображение
    private val _imageBitmap = MutableStateFlow<Bitmap?>(null);
    val imageBitmap: StateFlow<Bitmap?> = _imageBitmap

    // uri для галереи
    private val _imageUri = MutableStateFlow<Uri?>(null);
    val imageUri: StateFlow<Uri?> = _imageUri


    // url загруженного изображения
    private val _imageUrl = MutableStateFlow<String?>(null);
    val imageUrl: StateFlow<String?> = _imageUrl

    // флаг, идет ли загрузка
    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading


    fun setImage(bitmap: Bitmap?, uri: Uri?){
        _imageBitmap.value = bitmap
        _imageUri.value = uri
    }

    fun uploadImage(context: Context){
        viewModelScope.launch {
            _isUploading.value = true
            try {
                val url = when {
                    _imageUri.value != null -> repository.uploadImageUri(context, _imageUri.value!!)
                    _imageBitmap.value != null -> repository.uploadImage(_imageBitmap.value!!)
                    else -> null
                }
                _imageUrl.value = url
            }catch (e: Exception){
                e.printStackTrace()
            } finally {
                _isUploading.value = false
            }
        }
    }
}