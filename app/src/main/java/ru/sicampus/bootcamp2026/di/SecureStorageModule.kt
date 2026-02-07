package ru.sicampus.bootcamp2026.di

import com.example.token_storage.data.SecureStorageImpl
import com.example.token_storage.domain.SecureStorage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SecureStorageModule{
    @Binds
    @Singleton
    abstract fun bindSecureStorageRepository(
        impl: SecureStorageImpl
    ): SecureStorage
}