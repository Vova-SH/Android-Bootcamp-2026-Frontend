package ru.sicampus.bootcamp2026.domain.entities

data class UserEntity(
    var surname: String = "",
    var name: String = "",
    var patronymic: String? = null,
    var avatarUrl: String? = null,
    var mail: String = "",
    var contacts: Map<String, String> = emptyMap(),
    var friends: List<String> = emptyList(),
    val token: String = ""
) {
    // Добавляем функции для удобства
    fun addFriend(friend: String): UserEntity {
        return this.copy(friends = friends + friend)
    }

    fun addContact(key: String, value: String): UserEntity {
        return this.copy(contacts = contacts + (key to value))
    }    // Добавляем функции для удобства
    fun removeFriend(friend: String): UserEntity {
        return this.copy(friends = friends - friend)
    }

    fun FIO() : String {
        return "$surname $name $patronymic"
    }

//    fun removeContact(key: String, value: String): UserEntity {
//        return this.copy(contacts = contacts - (key to value))
//    } TODO
}