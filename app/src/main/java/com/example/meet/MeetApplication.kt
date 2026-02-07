package com.example.meet

import android.app.Application
import com.example.meet.data.source.AuthPrefs

class MeetApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AuthPrefs.init(this)
    }
}