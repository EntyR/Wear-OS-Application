package com.harman.wearosapp.app

import android.app.Application
import com.harman.wearosapp.app.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class HeartRateApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@HeartRateApplication)
            modules(
                dataBaseModule,
                dataModule,
                censorModule,
                useCaseModule,
                viewModelModule
            )
        }

    }
}