package ru.sicampus.bootcamp2026.data

import ru.sicampus.bootcamp2026.data.source.SignInLocalDataSource
import ru.sicampus.bootcamp2026.data.source.SignInNetworkDataSource
import ru.sicampus.bootcamp2026.domain.users.entities.UserEntity

class SignInRepository(
    private val signInNetworkDataSource: SignInNetworkDataSource,
    private val signInLocalDataSource: SignInLocalDataSource
) {
    suspend fun checkAndSignIn(
        login: String,
        password: String
    ): Result<Boolean> {
        signInLocalDataSource.setToken(login, password)
        return signInNetworkDataSource.checkSignIn()
            .onSuccess { isLogin ->
                if (!isLogin) signInLocalDataSource.clearToken()
            }
            .onFailure {
                signInLocalDataSource.clearToken()
            }
    }
    suspend fun getCurrentUser(): Result<UserEntity> {
        return signInNetworkDataSource.getSignedUser().mapCatching { dto ->
            dto.mapNotNull { userDto ->
                UserEntity(
                    id = userDto.id ?: return@mapNotNull null,
                    name = userDto.name ?: return@mapNotNull null,
                    photoUrl = userDto.photoUrl ?: return@mapNotNull null,
                    email = userDto.email ?: return@mapNotNull null,
                    surname = userDto.surname ?: return@mapNotNull null,
                    patronymic = userDto.patronymic ?: return@mapNotNull null,
                    username = userDto.username ?: return@mapNotNull null,
                    messengerLink = userDto.messengerLink ?: return@mapNotNull null,
                    phoneNumber = userDto.phoneNumber ?: return@mapNotNull null,
                    departmentName = userDto.departmentName ?: return@mapNotNull null,
                )
            }.first()
        }
    }

}