package com.apkakisan.myapplication

import android.app.Application
import com.apkakisan.myapplication.di.appModule
import com.zeugmasolutions.localehelper.LocaleAwareApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ApkaKisanApp :LocaleAwareApplication() {

    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin{
            androidLogger()
            androidContext(this@ApkaKisanApp)
            modules(appModule)
        }
    }
}