package com.example.planify.core.exceptions

class AlreadyExistsAppError(message: String) : ApplicationException(message)
class NotFoundAppError(message: String) : ApplicationException(message)
class UnauthenticatedAppError(message: String): ApplicationException(message)
