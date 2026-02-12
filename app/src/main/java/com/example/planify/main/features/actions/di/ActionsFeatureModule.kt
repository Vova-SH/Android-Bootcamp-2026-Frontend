package com.example.planify.main.features.actions.di

import android.content.Context
import com.example.planify.main.common.db.AppDatabase
import com.example.planify.main.features.actions.data.dao.ActionModelDAO
import com.example.planify.main.features.actions.data.sources.ActionsLocalDataSource
import com.example.planify.main.features.actions.data.sources_impl.ActionsLocalDataSourceImpl
import com.example.planify.main.features.actions.domain.utils.ActionDataParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ActionsFeatureModule {
    @Provides
    @Singleton
    fun provideActionDataParser(): ActionDataParser = ActionDataParser()

    @Provides
    @Singleton
    fun provideActionModelDAO(
        appDatabase: AppDatabase
    ): ActionModelDAO = appDatabase.actionModelDAO()

    @Provides
    @Singleton
    fun provideActionsLocalDataSource(
        @ApplicationContext context: Context,
        actionModelDAO: ActionModelDAO
    ): ActionsLocalDataSource {
        return ActionsLocalDataSourceImpl(
            context = context,
            actionModelDAO = actionModelDAO
        )
    }
}
