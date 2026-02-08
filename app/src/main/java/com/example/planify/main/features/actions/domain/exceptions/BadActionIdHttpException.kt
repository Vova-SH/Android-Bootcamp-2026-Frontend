package com.example.planify.main.features.actions.domain.exceptions

import com.example.planify.main.common.network.exceptions.ApplicationHttpException
import io.ktor.http.HttpStatusCode

class BadActionIdHttpException(
    message: String?,
    appCode: Int = 6100
) : ApplicationHttpException(
    httpStatus = HttpStatusCode.Forbidden,
    appCode = appCode,
    message = message
)
