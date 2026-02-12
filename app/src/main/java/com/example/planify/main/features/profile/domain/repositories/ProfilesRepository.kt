package com.example.planify.main.features.profile.domain.repositories

import com.example.planify.main.features.profile.entities.Profile

interface ProfilesRepository {
    fun getMyProfile(): Profile

    suspend fun fetchMyProfile(): Result<Profile>

    suspend fun fetchUsersProfile(userIds: List<Long>): List<Profile>

    suspend fun fetchUserProfile(userId: Long): Profile
}