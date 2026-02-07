//package ru.sicampus.bootcamp2026.domain.usecase
//
//import ru.sicampus.bootcamp2026.data.dto.user.UserMiniDto
//import ru.sicampus.bootcamp2026.data.repository.MeetingRepository
//import ru.sicampus.bootcamp2026.data.repository.UserRepository
//
//class CreateMeetingUseCase(
//    private val meetingRepository: MeetingRepository,
//    private val userRepository: UserRepository
//) {
//    suspend operator fun invoke(meetingData: Map<String, Any>): Result<Unit> {
//        return meetingRepository.createMeeting(meetingData).map { Unit }
//    }
//
//    // мне кажется опционально
//    suspend fun searchUsers(query: String): Result<List<UserMiniDto>> {
//        return userRepository.searchUsers(query)
//    }
//}