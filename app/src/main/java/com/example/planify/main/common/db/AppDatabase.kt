package com.example.planify.main.common.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.planify.main.features.actions.data.dao.ActionModelDAO
import com.example.planify.main.features.actions.data.models.ActionModel
import javax.inject.Singleton

@Singleton
@Database(
    entities = [ActionModel::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun actionModelDAO(): ActionModelDAO
}
