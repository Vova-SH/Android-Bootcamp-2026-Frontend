package com.example.planify.main.features.settings.di

import com.example.planify.main.features.settings.data.repositories_impl.SettingsRepositoryImpl
import com.example.planify.main.features.settings.domain.repositories.SettingsRepository
import com.example.planify.main.features.settings.domain.services.SettingsService
import com.example.planify.main.features.settings.domain.services_impl.SettingsServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsFeatureBindingModule {
    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindSettingsService(impl: SettingsServiceImpl): SettingsService


}
