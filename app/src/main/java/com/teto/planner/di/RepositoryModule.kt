package com.teto.planner.di

import com.teto.planner.data.repository.AuthRepositoryImpl
import com.teto.planner.data.repository.MeetingRepositoryImpl
import com.teto.planner.data.repository.RoomRepositoryImpl
import com.teto.planner.data.repository.UserRepositoryImpl
import com.teto.planner.domain.repository.AuthRepository
import com.teto.planner.domain.repository.MeetingRepository
import com.teto.planner.domain.repository.RoomRepository
import com.teto.planner.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindMeetingRepository(
        impl: MeetingRepositoryImpl
    ): MeetingRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindRoomRepository(
        impl: RoomRepositoryImpl
    ): RoomRepository
}