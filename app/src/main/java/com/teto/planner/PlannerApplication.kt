package com.teto.planner

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PlannerApplication : Application()

// todo list:
// todo удалить todo list
// todo сделать очистку форм при тряске устройства
// todo виброотклик на какие-то из кнопок можно
// todo запретить переворот экрана, это нафиг не нужно пользователю
// todo подумать про usecases. по сути это соблюдение clean arch, но это over engineering имхо - просто шаблонный код, много файлов, спросить у вовчика
// todo сделать ключ для подписи релизной апк