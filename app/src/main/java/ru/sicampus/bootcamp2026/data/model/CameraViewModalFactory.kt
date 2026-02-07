package ru.sicampus.bootcamp2026.data.model


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.sicampus.bootcamp2026.data.camera.ImageRepository

@Suppress("UNCHECKED_CAST")
class CameraViewModalFactory(private val repository: ImageRepository)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CameraViewModal::class.java)){
            return CameraViewModal(repository) as T
        }
        throw IllegalArgumentException("Not CameraViewModal")
    }
}