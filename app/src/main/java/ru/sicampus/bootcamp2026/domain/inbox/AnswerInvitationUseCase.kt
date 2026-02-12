package ru.sicampus.bootcamp2026.domain.inbox

import ru.sicampus.bootcamp2026.data.repository.InvitationRepository

class AnswerInvitationUseCase(
    private val repo: InvitationRepository
) {
    suspend operator fun invoke(id : Int, isAccepted : Boolean) : Result<Unit>{
        return repo.answerInvitation(id, isAccepted)
    }
}