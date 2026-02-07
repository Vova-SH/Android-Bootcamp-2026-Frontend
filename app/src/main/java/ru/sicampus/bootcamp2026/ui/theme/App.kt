package ru.sicampus.bootcamp2026.ui.theme

import android.app.Application
import android.content.Context
import ru.sicampus.bootcamp2026.data.source.Network

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Network.init(this)
        context = this
    }
    companion object {
        lateinit var context: Context
    }
}