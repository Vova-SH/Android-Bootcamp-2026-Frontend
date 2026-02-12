package com.example.planify.main.common.network.policies.app_code

interface AppCodeProcessingPolicy {
    fun process(appCode: Int, message: String)
}
