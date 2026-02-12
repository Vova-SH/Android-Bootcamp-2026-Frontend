package ru.sicampus.bootcamp2026.core

object Constants {

    const val BASE_URL = "http://192.168.1.14:8080"

    // auth
    const val LOGIN_ENDPOINT = "/api/v1/auth/login"
    const val REGISTER_ENDPOINT = "/api/v1/auth/register"

    // user
    const val GET_BY_ID_ENDPOINT = "/api/v1/users"
    const val SEARCH_USERS_ENDPOINT = "/api/v1/users/search"
    const val UPDATE_USER_ENDPOINT = "/api/v1/users"

    // meeting
    const val MEETING_ENDPOINT = "api/v1/meetings"
    const val SHEDULE_ENDPOINT = "api/v1/meetings/schedule"

    const val DAY_SCHEDULE_ENDPOINT = "/api/v1/meetings/schedule/day"

    // invitation
    const val INVITATION_ENDPOINT = "/api/v1/invitation"
}

/*
object Config {
    const val BASE_URL = "http://192.168.1.11:8080"

    // Auth endp-s
    const val LOGIN_ENDPOINT = "/auth/login"
    const val REGISTER_ENDPOINT = "/auth/register"
    const val REFRESH_TOKEN_ENDPOINT = "/auth/refresh"

    // User endp-s
    const val USER_BY_ID_ENDPOINT = "/users/{id}"
    const val SEARCH_USERS_ENDPOINT = "/users/search"
    const val UPDATE_USER_ENDPOINT = "/users/{id}"

    // Meeting endp-s
    const val CREATE_MEETING_ENDPOINT = "/meetings"
    const val GET_MEETING_BY_ID_ENDPOINT = "/meetings/{id}"
    const val GET_DAY_SCHEDULE_ENDPOINT = "/meetings/schedule/day"
    const val GET_WEEK_SCHEDULE_ENDPOINT = "/meetings/schedule/week"
    const val GET_MONTH_SCHEDULE_ENDPOINT = "/meetings/schedule/month"
    const val GET_INVITATIONS_ENDPOINT = "/meetings/invitations"
    const val RESPOND_TO_INVITATION_ENDPOINT = "/meetings/{id}/respond"

    // Headers
    const val AUTHORIZATION_HEADER = "Authorization"
    const val BEARER_PREFIX = "Bearer "
    const val CONTENT_TYPE_HEADER = "Content-Type"
    const val CONTENT_TYPE_JSON = "application/json"

    // Status codes
    const val HTTP_UNAUTHORIZED = 401
    const val HTTP_FORBIDDEN = 403
}
* */