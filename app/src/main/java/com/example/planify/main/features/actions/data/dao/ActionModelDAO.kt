package com.example.planify.main.features.actions.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.planify.main.features.actions.data.models.ActionModel

@Dao
interface ActionModelDAO {
    @Upsert
    suspend fun upsert(model: ActionModel)

    @Query("SELECT * FROM actions")
    suspend fun getAll(): List<ActionModel>

    @Query("DELETE FROM actions WHERE id = :actionId")
    suspend fun deleteActionById(actionId: String)
}
