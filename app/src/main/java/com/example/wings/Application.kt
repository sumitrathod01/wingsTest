package com.example.wings

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.techfirst.marksmentor.support.utils.SharedPreferenceUtil

class Application : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        SharedPreferenceUtil.init(this)
        mInstance = this
    }

    companion object {
        private var mInstance: Application? = null

        val instance: Application?
            @Synchronized get() {
                var appSingleton: Application?
                synchronized(Application::class.java)
                {
                    appSingleton = mInstance
                }
                return appSingleton
            }
    }
}