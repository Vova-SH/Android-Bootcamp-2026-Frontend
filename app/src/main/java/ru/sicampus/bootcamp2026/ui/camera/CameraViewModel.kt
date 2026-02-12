package ru.sicampus.bootcamp2026.ui.camera


import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.http.Url
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.ImageRepository
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource

class CameraViewModal(private val repository: ImageRepository) : ViewModel(){

    private val _userId = MutableStateFlow<Int?>(null)
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

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun setUserId(id: Int) {
        _userId.value = id
    }
    fun setImage(bitmap: Bitmap?, uri: Uri?){
        _imageBitmap.value = bitmap
        _imageUri.value = uri
        _errorMessage.value = null
    }

    fun uploadImage(context: Context){
        val userId = _userId.value
        Log.d("UPLOAD", "uploadImage called")
        if (userId == null) {
            Log.e("UPLOAD", "ABORT: userId is null")
            _errorMessage.value = "Вы не авторизованы"
            return
        }
        viewModelScope.launch {
            _isUploading.value = true
            _errorMessage.value = null
            try {
                Log.d("UPLOAD", "uri=${_imageUri.value}, bitmap=${_imageBitmap.value}")

                val url = when {
                    _imageUri.value != null -> repository.uploadImageUri(context,_imageUri.value!!, userId)
                    _imageBitmap.value != null -> repository.uploadImage( _imageBitmap.value!!, userId)
                    else -> null
                }
                if (url == null) {
                    _errorMessage.value = "Не удалось загрузить изображение"
                } else {
                    _imageUrl.value = url
                    Log.d("UPLOAD", "Upload successful! URL: $url")

                }
            }catch (e: Exception){
                Log.e("UPLOAD", "Upload failed", e)
                e.printStackTrace()
                _errorMessage.value = "Ошибка загрузки: ${e.message}"
            } finally {
                _isUploading.value = false
            }
        }
    }
    fun loadProfileImage() {
        val userId = _userId.value ?: return
        Log.d("PROFILE", "Loaded profile image URL: $userId")
        viewModelScope.launch {
            _isUploading.value = true
            try {
                val url = repository.getProfileImageUrl(userId)
                _imageUrl.value = url
                Log.d("PROFILE", "Loaded profile image URL: $url")
            } catch (e: Exception) {
                Log.e("PROFILE", "Failed to load profile image", e)
            } finally {
                _isUploading.value = false
            }
        }
    }
}