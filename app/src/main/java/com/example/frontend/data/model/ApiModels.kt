package com.example.frontend.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PersonShortDTO(
    val id: Long,
    val name: String,
    val login: String,
    @Json(name = "photoUrl")
    val photo: String? = null,
    @Json(name = "departmentName")
    val dept: String? = null
)

@JsonClass(generateAdapter = true)
data class PersonWithInvitesDTO(
    val id: Long,
    val name: String,
    val login: String,
    @Json(name = "photoUrl")
    val photo: String? = null,
    @Json(name = "departmentName")
    val dept: String? = null,
    val meetups: List<MeetupDTO>? = null,
    val invites: List<InviteWithMeetupDTO>? = null
)

@JsonClass(generateAdapter = true)
data class PersonRegisterDTO(
    val id: Long? = null,
    val name: String,
    val login: String,
    val password: String,
    @Json(name = "departmentName")
    val dept: String
)

@JsonClass(generateAdapter = true)
data class MeetupDTO(
    val id: Long,
    val date: String,
    val time: String,
    val planner: PersonShortDTO
)

@JsonClass(generateAdapter = true)
data class MeetupShortDTO(
    val id: Long? = null,
    val date: String,
    val time: String
)

@JsonClass(generateAdapter = true)
data class MeetupWithInvitesDTO(
    val id: Long,
    val date: String,
    val time: String,
    val planner: PersonShortDTO,
    val invites: List<InviteWithPersonDTO>? = null
)

@JsonClass(generateAdapter = true)
data class MeetupToCreateDTO(
    val id: Long? = null,
    val date: String,
    val time: String,
    @Json(name = "planner_id")
    val plannerId: Long
)

@JsonClass(generateAdapter = true)
data class InviteDTO(
    val id: Long,
    val agree: Boolean,
    val meetup: MeetupDTO,
    val participant: PersonShortDTO
)

@JsonClass(generateAdapter = true)
data class InviteCreateDTO(
    val id: Long? = null,
    val agree: Boolean = false,
    @Json(name = "meetup_id")
    val meetupId: Long,
    @Json(name = "participant_id")
    val participantId: Long
)

@JsonClass(generateAdapter = true)
data class InviteWithMeetupDTO(
    val id: Long,
    val agree: Boolean,
    val meetup: MeetupDTO
)

@JsonClass(generateAdapter = true)
data class InviteWithPersonDTO(
    val id: Long,
    val agree: Boolean,
    val participant: PersonShortDTO
)

@JsonClass(generateAdapter = true)
data class DepartmentDTO(
    val id: Long? = null,
    val name: String
)

@JsonClass(generateAdapter = true)
data class PageMetadata(
    val size: Long,
    val number: Long,
    val totalElements: Long,
    val totalPages: Long
)

@JsonClass(generateAdapter = true)
data class PagedModelPersonWithInvitesDTO(
    val content: List<PersonWithInvitesDTO>,
    val page: PageMetadata
)

@JsonClass(generateAdapter = true)
data class LoginRequest(
    val login: String,
    val password: String
)

@JsonClass(generateAdapter = true)
data class LoginResponse(
    val token: String? = null,
    val person: PersonWithInvitesDTO? = null
)
