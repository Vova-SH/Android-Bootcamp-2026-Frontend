package com.example.planify.main.features.profile.domain.services_impl

import com.example.planify.core.SingletonHolder
import com.example.planify.main.features.profile.domain.repositories.ProfilesRepository
import com.example.planify.main.features.profile.domain.services.ProfilesService
import com.example.planify.main.features.profile.entities.Profile

class ProfilesServiceImplST private constructor(
    val profileService: ProfilesRepository
): ProfilesService {
    override fun getMyProfile(): Profile {
        return profileService.getMyProfile()
    }

    override suspend fun fetchMyProfile(): Result<Profile> {
        return profileService.fetchMyProfile()
    }

    override suspend fun fetchUsersProfile(userIds: List<Long>): List<Profile> {
        return profileService.fetchUsersProfile(userIds)
    }

    override suspend fun fetchUserProfile(userId: Long): Profile {
        return profileService.fetchUserProfile(userId)
    }

    companion object : SingletonHolder<ProfilesServiceImplST, ProfilesRepository>(::ProfilesServiceImplST)
}