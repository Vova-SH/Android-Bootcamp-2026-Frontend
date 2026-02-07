package ru.sicampus.bootcamp2026.ui.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.sicampus.bootcamp2026.data.repository.ImageRepository

@Suppress("CAST_NEVER_SUCCEEDS")
class CameraViewModelFactory(private val repository: ImageRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CameraViewModel::class.java)) {
            return CameraViewModelFactory(repository) as T
        }
        throw IllegalArgumentException("Not CameraViewModel")
    }
}