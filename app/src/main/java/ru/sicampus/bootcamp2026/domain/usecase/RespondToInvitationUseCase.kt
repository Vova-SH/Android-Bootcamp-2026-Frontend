//package ru.sicampus.bootcamp2026.domain.usecase
//
//import ru.sicampus.bootcamp2026.data.repository.MeetingRepository
//
//class RespondToInvitationUseCase(
//    private val meetingRepository: MeetingRepository
//) {
//    suspend operator fun invoke(meetingId: Long, response: Boolean): Result<Unit> {
//        return meetingRepository.respondToInvitation(meetingId, response)
//    }
//}