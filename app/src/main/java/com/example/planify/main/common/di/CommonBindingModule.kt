package com.example.planify.main.common.di

import com.example.planify.main.common.network.policies.app_code.AppCodeProcessingPolicy
import com.example.planify.main.common.network.policies.app_code.AppCodeProcessingPolicyImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CommonBindingModule {
    @Binds
    @Singleton
    abstract fun bindAppCodeProcessingPolicy(impl: AppCodeProcessingPolicyImpl): AppCodeProcessingPolicy
}
