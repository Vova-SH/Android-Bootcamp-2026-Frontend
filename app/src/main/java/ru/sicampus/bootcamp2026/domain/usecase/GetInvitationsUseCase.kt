//package ru.sicampus.bootcamp2026.domain.usecase
//
//import ru.sicampus.bootcamp2026.data.dto.meeting.MeetingMiniDto
//import ru.sicampus.bootcamp2026.data.repository.MeetingRepository
//
//class GetInvitationsUseCase(
//    private val meetingRepository: MeetingRepository
//) {
//    suspend operator fun invoke(status: String = "pending"): Result<List<MeetingMiniDto>> {
//        return meetingRepository.getInvitations(status)
//    }
//}