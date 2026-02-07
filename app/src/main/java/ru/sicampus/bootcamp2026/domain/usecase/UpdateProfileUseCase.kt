//package ru.sicampus.bootcamp2026.domain.usecase
//
//import ru.sicampus.bootcamp2026.data.dto.user.UserUpdateDto
//import ru.sicampus.bootcamp2026.data.repository.UserRepository
//
//class UpdateProfileUseCase(
//    private val userRepository: UserRepository
//) {
//    suspend operator fun invoke(userUpdateDto: UserUpdateDto): Result<Unit> {
//        return userRepository.updateUser(userUpdateDto).map { Unit }
//    }
//}