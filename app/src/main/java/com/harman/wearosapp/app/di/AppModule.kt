package com.harman.wearosapp.app.di

import androidx.health.services.client.HealthServices
import com.harman.wearosapp.data.data_source.HealthServicesManager
import com.harman.wearosapp.data.repository.HealthRepository
import com.harman.wearosapp.domain.repository.IHealthRepository
import com.harman.wearosapp.domain.use_cases.HRUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single {
        HealthServices.getClient(androidContext())
    }
    single { HealthServicesManager(get()) }

    single<IHealthRepository> {
        HealthRepository(get())
    }
}

val useCaseModule = module {
    factory { HRUseCase(get()) }
}