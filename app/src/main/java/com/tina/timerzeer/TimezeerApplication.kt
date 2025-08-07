package com.tina.timerzeer

import android.app.Application
import com.tina.timerzeer.di.initKoin
import com.tina.timerzeer.di.timerModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class TimezeerApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@TimezeerApplication)
            androidLogger(Level.DEBUG)
            modules(timerModule)
        }
    }
}