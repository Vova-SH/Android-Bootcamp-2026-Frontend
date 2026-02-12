package com.example.planify.core.network.exceptions

import com.example.planify.core.exceptions.ApplicationException

class ApiRequestFailedAppError(
    message: String
) : ApplicationException(
    message = message
)
