package ru.sicampus.bootcamp2026.di

import com.example.create_meet.domain.MeetingRepository
import com.example.create_meet.domain.MeetingRepositoryImpl
import com.example.user_main.domain.UserRepository
import com.example.user_main.domain.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserMainModule {
    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class MeetingModule {
    @Binds
    @Singleton
    abstract fun bindMeetingRepository(
        impl: MeetingRepositoryImpl
    ): MeetingRepository
}