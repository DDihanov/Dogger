package com.dihanov.di

import com.dihanov.util.KeyboardManager
import org.koin.dsl.module

val utilModule = module {
    single { KeyboardManager() }
}