package ru.sicampus.bootcamp2026.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.sicampus.bootcamp2026.data.repository.AuthRepositoryImpl
import ru.sicampus.bootcamp2026.data.repository.InvitationRepositoryImpl
import ru.sicampus.bootcamp2026.data.repository.MeetingRepositoryImpl
import ru.sicampus.bootcamp2026.data.repository.ProfileRepositoryImpl
import ru.sicampus.bootcamp2026.domain.repository.AuthRepository
import ru.sicampus.bootcamp2026.domain.repository.InvitationRepository
import ru.sicampus.bootcamp2026.domain.repository.MeetingRepository
import ru.sicampus.bootcamp2026.domain.repository.ProfileRepository
import javax.inject.Singleton

/**
 * Модуль Hilt для предоставления репозиториев
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        profileRepositoryImpl: ProfileRepositoryImpl
    ): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindMeetingRepository(
        meetingRepositoryImpl: MeetingRepositoryImpl
    ): MeetingRepository

    @Binds
    @Singleton
    abstract fun bindInvitationRepository(
        invitationRepositoryImpl: InvitationRepositoryImpl
    ): InvitationRepository
}

