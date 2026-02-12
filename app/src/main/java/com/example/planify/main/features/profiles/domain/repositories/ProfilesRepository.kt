package com.example.planify.main.features.profiles.domain.repositories

import com.example.planify.main.features.profiles.domain.entities.Page
import com.example.planify.main.features.profiles.domain.entities.Profile
import com.example.planify.main.features.profiles.domain.schemas.PatchMyProfileSchema
import com.example.planify.main.features.profiles.domain.schemas.PutMyProfileSchema
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

interface ProfilesRepository {
    val myProfile: StateFlow<Profile?>
    suspend fun fetchMyProfile(): Result<Profile>

    suspend fun patchMyProfile(shema: PatchMyProfileSchema): Result<Unit>

    suspend fun putMyProfile(shema: PutMyProfileSchema): Result<Unit>

    suspend fun searchProfile(query: String, page: Int? = null, size: Int? = null, sort: List<String>? = null): Result<Page>
}
