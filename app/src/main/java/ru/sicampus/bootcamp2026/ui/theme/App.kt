package ru.sicampus.bootcamp2026.ui.theme

import android.app.Application
import android.content.Context
import ru.sicampus.bootcamp2026.data.source.Network


//class App : Application() {
//    companion object {
//        private lateinit var appInstance: App
//
//        fun getAppContext(): Context = appInstance.applicationContext
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        appInstance = this
//    }
//}
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        //Network.init(this)
      context = this
    }
    companion object {
        lateinit var context: Context
    }
}
//class App : Application() {
//    override fun onCreate() {
//        super.onCreate()
//        INSTANCE = this
//        Network.init(this)
//    }
//
//    companion object {
//        private var INSTANCE: App? = null
//        val context: Context
//            get() = INSTANCE?.applicationContext
//                ?: throw IllegalStateException("App.context is not initialized yet")
//    }
//}