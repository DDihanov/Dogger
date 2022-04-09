package com.dihanov.dogger

import android.app.Application
import com.dihanov.catsearch.di.*
import com.dihanov.di.utilModule
import com.dihanov.dogcatcombined.di.combinedUseCaseModule
import com.dihanov.dogcatcombined.di.combinedViewModelModule
import com.dihanov.dogsearch.di.*
import com.dihanov.util.AppLogger
import com.example.db.di.daoModule
import com.example.db.di.dbModule
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
                utilModule,
                daoModule,
                dbModule,

                dogRepository,
                dogViewModelModule,
                dogUseCaseModule,
                dogMapperModule,
                dogApiModule,
                dogRetrofitModule,

                catRepository,
                catViewModelModule,
                catUseCaseModule,
                catMapperModule,
                catApiModule,
                catRetrofitModule,

                combinedViewModelModule,
                combinedUseCaseModule
            ))
        }
    }
}