package ru.sicampus.bootcamp2026.android.domain.invitation

import ru.sicampus.bootcamp2026.android.data.CreateMeetingRepository
import ru.sicampus.bootcamp2026.android.data.source.PersonResponse

class SearchPersonByEmailUseCase(
    private val repository: CreateMeetingRepository
) {
    suspend operator fun invoke(query: String): Result<List<PersonResponse>> {
        if (query.length < 3) {
            return Result.success(emptyList())
        }

        return repository.searchPersonsByPartialEmail(query)
    }
}