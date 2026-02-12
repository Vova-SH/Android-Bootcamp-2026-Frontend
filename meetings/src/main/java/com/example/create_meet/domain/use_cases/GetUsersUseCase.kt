package com.example.create_meet.domain.use_cases

import com.example.comon.UserDto
import com.example.create_meet.domain.MeetingRepository
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: MeetingRepository
) {
    suspend operator fun invoke(): Result<List<UserDto>> =
        repository.getUsers()
}

