package com.example.planify.main.features.auth.di

import com.example.planify.main.features.auth.data.repositories_impl.AuthRepositoryImpl
import com.example.planify.main.features.auth.data.repositories_impl.SessionsRepositoryImpl
import com.example.planify.main.features.auth.data.repositories_impl.UsersRepositoryImpl
import com.example.planify.main.features.auth.data.sources.AuthRemoteDataSource
import com.example.planify.main.features.auth.data.sources_impl.AuthRemoteDataSourceImpl
import com.example.planify.main.features.auth.domain.AuthTokenManager
import com.example.planify.main.features.auth.domain.repositories.AuthRepository
import com.example.planify.main.features.auth.domain.repositories.SessionsRepository
import com.example.planify.main.features.auth.domain.repositories.UsersRepository
import com.example.planify.main.features.auth.domain.services.AuthService
import com.example.planify.main.features.auth.domain.services_impl.AuthServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthFeatureBindingModule {
    @Binds
    @Singleton
    abstract fun bindAuthRemoteDataSource(impl: AuthRemoteDataSourceImpl): AuthRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindSessionsRepository(impl: SessionsRepositoryImpl): SessionsRepository

    @Binds
    @Singleton
    abstract fun bindUsersRepository(impl: UsersRepositoryImpl): UsersRepository

    @Binds
    @Singleton
    abstract fun bindAuthService(authService: AuthServiceImpl): AuthService

    @Binds
    @Singleton
    abstract fun bindTokenManager(authService: AuthServiceImpl): AuthTokenManager
}
