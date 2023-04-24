package com.androidvibes.mindbodytest

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MindBodyApp : Application() {

    companion object {
        @JvmStatic
        lateinit var appContext: MindBodyApp
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}