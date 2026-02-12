package com.example.planify.main.navigation.screens.create_meeting_screen

import com.example.planify.core.ui.pager_router_screen.PagerRoute

sealed class CreateMeetingRoute(override val key: String) : PagerRoute {
    object Info : CreateMeetingRoute("step1")
    object Time : CreateMeetingRoute("step2")
    object Participants : CreateMeetingRoute("step3")

    companion object {
        val routes: List<CreateMeetingRoute> = listOf(Info, Time, Participants)
    }
}