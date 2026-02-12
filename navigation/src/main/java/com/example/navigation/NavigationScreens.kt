package com.example.navigation

import kotlin.math.round

enum class NavigationScreens (
    val routeName: String
){
    AUTHORIZATION(routeName = "authorization"),
    REGISTER(routeName = "register"),
    MAIN(routeName = "main"),
    ADD_NEW_MEETING(routeName = "add_new_meeting")
}