package com.example.planify.main.common.db.di

import android.content.Context
import androidx.room.Room
import com.example.planify.main.common.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonDBModule {
    @Singleton
    @Provides
    fun provideAppDataBase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context = context.applicationContext,
            klass = AppDatabase::class.java,
            name = "app_db"
        ).fallbackToDestructiveMigration(false).build()
    }
}
