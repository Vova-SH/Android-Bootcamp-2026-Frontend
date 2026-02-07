//package ru.sicampus.bootcamp2026.data.source
//
//import kotlinx.coroutines.delay
//import ru.sicampus.bootcamp2026.data.dto.UserDto
//
//class RegisterNetworkDataSource {
//    suspend fun register(
//        login: String,
//        password: String,
//        confirmPassword: String,
//        name: String,
//        lastName: String,
//        email: String,
//        phoneNumber: String,
//        department: String,
//        position: String,
//        photoUrl: String,
//    ): Result<UserDto> {
//        return try {
//            delay(timeMillis = 1000)
//
//            if (password != confirmPassword) {
//                return Result.failure(exception = _root_ide_package_.kotlinx.io.IOException("Пароли не совпадают"))
//            }
//
//            Result.success(
//                value = UserDto(
//                    id = 1,
//                    name = name,
//                    lastName = lastName,
//                    phoneNumber = phoneNumber,
//                    login = login,
//                    email = email,
//                    password = password,
//                    department = department,
//                    position = position,
//                    photoUrl = photoUrl,
//                )
//            )
//        } catch (e: Exception) {
//            Result.failure(exception = e)
//        }
//    }
//}