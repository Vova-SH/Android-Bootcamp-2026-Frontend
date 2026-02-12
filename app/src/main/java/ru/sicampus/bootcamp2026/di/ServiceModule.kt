package ru.sicampus.bootcamp2026.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.sicampus.bootcamp2026.data.service.ImageLoaderServiceImpl
import ru.sicampus.bootcamp2026.domain.service.ImageLoaderService
import javax.inject.Singleton

/**
 * Модуль DI для сервисов приложения
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Binds
    @Singleton
    abstract fun bindImageLoaderService(
        imageLoaderServiceImpl: ImageLoaderServiceImpl
    ): ImageLoaderService
}

