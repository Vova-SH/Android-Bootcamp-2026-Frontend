package com.example.planify.main.common.network.exceptions

import com.example.planify.core.exceptions.ApplicationException
import io.ktor.http.HttpStatusCode

open class ApplicationHttpException(
    val httpStatus: HttpStatusCode,
    val appCode: Int,
    message: String?
) : ApplicationException(message ?: httpStatus.description)
