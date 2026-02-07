package com.teto.planner.presentation.features.auth

import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

// todo - сейчас state сохраняется после входа
// кейс: зашел в приложение -> выход -> на экране входа уже введены твои данные
// спросить у вовчика, это баг или фича
@Singleton
class AuthSharedState @Inject constructor() {
    val login = MutableStateFlow("")
    val password = MutableStateFlow("")
    val name = MutableStateFlow("")
    val telegramNick = MutableStateFlow("")

    fun clear() {
        login.value = ""
        password.value = ""
        name.value = ""
        telegramNick.value = ""
    }
}