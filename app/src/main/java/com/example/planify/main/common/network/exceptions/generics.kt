package com.example.planify.main.common.network.exceptions

import io.ktor.http.HttpStatusCode


open class InternalServerErrorHttpException(
    message: String?,
    appCode: Int = 2000,
) : ApplicationHttpException(
    httpStatus = HttpStatusCode.Conflict,
    appCode = appCode,
    message = message
)

open class AlreadyExistsHttpException(
    message: String?,
    appCode: Int = 2001,
) : ApplicationHttpException(
    httpStatus = HttpStatusCode.Conflict,
    appCode = appCode,
    message = message
)

open class NotFoundHttpException(
    message: String?,
    appCode: Int = 2002
) : ApplicationHttpException(
    httpStatus = HttpStatusCode.NotFound,
    appCode = appCode,
    message = message
)

open class BadRequestHttpException(
    message: String?,
    appCode: Int = 2005
) : ApplicationHttpException(
    httpStatus = HttpStatusCode.BadRequest,
    appCode = appCode,
    message = message
)

open class ForbiddenHttpException(
    message: String?,
    appCode: Int = 2006
) : ApplicationHttpException(
    httpStatus = HttpStatusCode.Forbidden,
    appCode = appCode,
    message = message
)

open class UnknownHttpException(
    message: String?,
    appCode: Int = -1
) : ApplicationHttpException(
    httpStatus = HttpStatusCode.Forbidden,
    appCode = appCode,
    message = message
)
