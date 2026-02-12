package ru.sicampus.bootcamp2026.data.repository

import ru.sicampus.bootcamp2026.data.dto.toDTO
import ru.sicampus.bootcamp2026.data.source.ProfileDataSource
import ru.sicampus.bootcamp2026.domain.entities.EmployeeEntity
import ru.sicampus.bootcamp2026.domain.entities.ProfileUpdateEntity

class ProfileRepository(
        private val profileDataSource: ProfileDataSource
) {
    suspend fun getProfile(): Result<EmployeeEntity>{
        return profileDataSource.getProfile().map{ dto ->
            EmployeeEntity(
                name = dto.name ?: "При получении данных произошла ошибка",
                position = dto.position ?: "",
                username = dto.username ?: "",
                email = dto.email ?: "",
                phoneNumber = dto.phoneNumber ?: "",
                photoUrl = dto.photoUrl
            )
        }
    }
    suspend fun editProfile(profileUpdateEntity: ProfileUpdateEntity): Result<Unit>{
        return profileDataSource.editProfile(profileUpdateEntity.toDTO())
    }
}
