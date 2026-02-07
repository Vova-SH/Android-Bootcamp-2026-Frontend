package com.example.navigation

import kotlin.math.round

enum class NavigationScreens (
    val routeName: String
){
    AUTHORIZATION(routeName = "authorization"),
    REGISTER(routeName = "register"),
    MAIN(routeName = "main"),
    PROFILE(routeName = "profile"),
}