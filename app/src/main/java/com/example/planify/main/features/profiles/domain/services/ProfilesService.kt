package com.example.planify.main.features.profiles.domain.services

import com.example.planify.main.features.profiles.domain.entities.Page
import com.example.planify.main.features.profiles.domain.entities.Profile
import com.example.planify.main.features.profiles.domain.schemas.PatchMyProfileSchema
import com.example.planify.main.features.profiles.domain.schemas.PutMyProfileSchema
import kotlinx.coroutines.flow.StateFlow

interface ProfilesService {
    val myProfile: StateFlow<Profile?>
    suspend fun fetchMyProfile(): Result<Profile>

    suspend fun patchMyProfile(shema: PatchMyProfileSchema): Result<Unit>

    suspend fun putMyProfile(shema: PutMyProfileSchema): Result<Unit>

    suspend fun searchProfile(query: String, page: Int? = null, size: Int? = null, sort: List<String>? = null): Result<Page>
}
