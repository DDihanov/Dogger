package com.example.db.di

import androidx.room.Room
import com.example.db.AppDb
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dbModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDb::class.java,
            "local_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}

val daoModule = module {
    single { get<AppDb>().dogDao() }
    single { get<AppDb>().catDao() }
}