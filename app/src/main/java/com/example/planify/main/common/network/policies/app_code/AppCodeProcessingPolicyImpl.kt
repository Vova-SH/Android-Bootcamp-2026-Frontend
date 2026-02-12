package com.example.planify.main.common.network.policies.app_code

import com.example.planify.main.common.network.exceptions.AlreadyExistsHttpException
import com.example.planify.main.common.network.exceptions.NotFoundHttpException
import com.example.planify.main.common.network.exceptions.UnknownHttpException
import com.example.planify.main.features.actions.domain.exceptions.BadActionIdHttpException
import jakarta.inject.Inject

class AppCodeProcessingPolicyImpl @Inject constructor() : AppCodeProcessingPolicy {
    override fun process(appCode: Int, message: String) {
        if (appCode in 1000..1999) return

        when (appCode) {
            2001 -> throw AlreadyExistsHttpException(message)
            2002 -> throw NotFoundHttpException(message)
            2003 -> throw NotFoundHttpException(message)
            6100 -> throw BadActionIdHttpException(message)
            else -> throw UnknownHttpException(message)
        }
    }
}
