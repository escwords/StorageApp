package com.words.storageapp

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import timber.log.Timber

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

    //here we fix the index memory compile time error message
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}