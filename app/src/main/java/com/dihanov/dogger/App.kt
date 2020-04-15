package com.dihanov.dogger

import android.app.Application
import com.dihanov.dogger.di.*
import com.dihanov.dogger.utils.AppLogger
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {


    override fun onCreate() {
        super.onCreate()
        AppLogger.init()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(
                viewModelModule,
                repositoryModule,
                apiModule,
                retrofitModule,
                dbModule,
                utilModule
            ))
        }
    }
}