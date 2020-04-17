package com.dihanov.util

import com.dihanov.base.BuildConfig
import timber.log.Timber
import timber.log.Timber.DebugTree


object AppLogger {
    fun d(s: String?, vararg objects: Any?) {
        Timber.d(s, *objects)
    }

    fun d(
        throwable: Throwable?,
        s: String?,
        vararg objects: Any?
    ) {
        Timber.d(throwable, s, *objects)
    }

    fun e(s: String?, vararg objects: Any?) {
        Timber.e(s, *objects)
    }

    fun e(
        throwable: Throwable?,
        s: String?,
        vararg objects: Any?
    ) {
        Timber.e(throwable, s, *objects)
    }

    fun i(s: String?, vararg objects: Any?) {
        Timber.i(s, *objects)
    }

    fun i(
        throwable: Throwable?,
        s: String?,
        vararg objects: Any?
    ) {
        Timber.i(throwable, s, *objects)
    }

    fun init() {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

    fun w(s: String?, vararg objects: Any?) {
        Timber.w(s, *objects)
    }

    fun w(
        throwable: Throwable?,
        s: String?,
        vararg objects: Any?
    ) {
        Timber.w(throwable, s, *objects)
    }
}