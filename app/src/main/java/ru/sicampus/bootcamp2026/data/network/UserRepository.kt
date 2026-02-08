package ru.sicampus.bootcamp2026.data.network

import ru.sicampus.bootcamp2026.data.network.source.UserInfoDataSource
import ru.sicampus.bootcamp2026.domain.entities.UserEntity

class UserRepository(
    private val userInfoDataSource: UserInfoDataSource
) {
    suspend fun getUsers(): Result<List<UserEntity>> {
        return userInfoDataSource.getUsers().map { listDto ->
            listDto.mapNotNull { userDto ->
                UserEntity(
                    surname = userDto.surname ?: return@mapNotNull null,
                    name = userDto.name ?: return@mapNotNull null,
                    patronymic = userDto.patronymic ?: "",
                    avatarUrl = userDto.avatar ?: return@mapNotNull null,
                    mail = userDto.mail ?: return@mapNotNull null,
                    contacts = userDto.contacts?.flatMap {
                        it.entries
                    }?.associate {
                        it.key to it.value
                    } ?: mutableMapOf(),
                    //                    friends = // TODO: на сервере эта функция ещё не реалихована, ждём
                )
            }
        }
    }

    suspend fun searchUsers(search: String): Result<List<UserEntity>> {
        return userInfoDataSource.searchUsers(search).map { listDto ->
            listDto.mapNotNull { userDto ->
                UserEntity(
                    surname = userDto.surname ?: return@mapNotNull null,
                    name = userDto.name ?: return@mapNotNull null,
                    patronymic = userDto.patronymic ?: return@mapNotNull null,
                    avatarUrl = userDto.avatar,
                    mail = userDto.mail ?: return@mapNotNull null,
                    contacts = userDto.contacts?.flatMap {
                        it.entries
                    }?.associate {
                        it.key to it.value
                    } ?: mutableMapOf(),
                )
            }
        }
    }

    suspend fun getUser(fio: String) : Result<UserEntity?> {
        return userInfoDataSource.getUser(fio).map { userDto ->
            val userDto = userDto.employees?.get(0)
            UserEntity(
                surname = userDto?.surname ?: "",
                name = userDto?.name ?: "",
                patronymic = userDto?.patronymic ?: "",
                avatarUrl = userDto?.avatar,
                mail = userDto?.mail ?: "",
                contacts = userDto?.contacts?.flatMap {
                    it.entries
                }?.associate {
                    it.key to it.value
                } ?: mutableMapOf(),
            )
        }
    }

    suspend fun getAuthUser(): Result<UserEntity> {
        return userInfoDataSource.getAuthUser().map { userDto ->
                UserEntity(
                    surname = userDto.surname ?: "",
                    name = userDto.name ?: "",
                    patronymic = userDto.patronymic ?: "",
                    avatarUrl = userDto.avatar,
                    mail = userDto.mail ?: "",
                    contacts = userDto.contacts?.flatMap {
                        it.entries
                    }?.associate {
                        it.key to it.value
                    } ?: mutableMapOf(),
                    //                    friends = // TODO: на сервере эта функция ещё не реалихована, ждём
                )
        }
    }
}

