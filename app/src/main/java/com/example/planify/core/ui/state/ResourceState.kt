package com.example.planify.core.ui.state

sealed class ResourceState<out T> {
    object Idle : ResourceState<Nothing>()
    object Loading : ResourceState<Nothing>()
    object Refreshing : ResourceState<Nothing>()
    data class Success<T>(val data: T) : ResourceState<T>()
    data class Error(val throwable: Throwable?) : ResourceState<Nothing>()
}
