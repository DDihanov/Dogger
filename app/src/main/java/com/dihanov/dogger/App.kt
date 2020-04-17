package com.dihanov.dogger

import android.app.Application
import com.dihanov.base_data.di.*
import com.dihanov.catsearch.di.catRepository
import com.dihanov.catsearch.di.catUseCaseModule
import com.dihanov.catsearch.di.catViewModelModule
import com.dihanov.di.utilModule
import com.dihanov.dogger.di.combinedUseCaseModule
import com.dihanov.dogger.di.combinedViewModelModule
import com.dihanov.dogsearch.di.dogRepository
import com.dihanov.dogsearch.di.dogUseCaseModule
import com.dihanov.dogsearch.di.dogViewModelModule
import com.dihanov.util.AppLogger
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
                dbModule,
                mapperModule,
                daoModule,

                dogRepository,
                dogViewModelModule,
                dogUseCaseModule,
                dogApiModule,
                dogRetrofitModule,

                catRepository,
                catViewModelModule,
                catUseCaseModule,
                catApiModule,
                catRetrofitModule,

                combinedViewModelModule,
                combinedUseCaseModule
            ))
        }
    }
}