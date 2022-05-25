package com.harman.wearosapp.app.di

import android.app.Application
import androidx.health.services.client.HealthServices
import androidx.room.Room
import com.harman.wearosapp.app.other.DATABASE_NAME
import com.harman.wearosapp.app.ui.heart_rate_screen.HeartRateViewModel
import com.harman.wearosapp.app.ui.hr_export_screen.ExportViewModel
import com.harman.wearosapp.data.dao.IHRDao
import com.harman.wearosapp.data.data_source.HealthServicesManager
import com.harman.wearosapp.data.db.HRDataBase
import com.harman.wearosapp.data.repository.DocumentsRepository
import com.harman.wearosapp.data.repository.HealthRepository
import com.harman.wearosapp.domain.repository.IDocumentsRepository
import com.harman.wearosapp.domain.repository.IHealthRepository
import com.harman.wearosapp.domain.use_cases.DocumentsUseCase
import com.harman.wearosapp.domain.use_cases.HRUseCase
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun providesDatabase(application: Application): HRDataBase {
    return Room.databaseBuilder(application, HRDataBase::class.java, DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .build()
}

fun providesHRDao(database: HRDataBase): IHRDao {
    return database.hrDao()
}

val dataBaseModule = module {
    single { providesDatabase(androidApplication()) }
    single { providesHRDao(get()) }
}
val viewModelModule = module {
    viewModel {
        HeartRateViewModel(get())
    }
    viewModel {
        ExportViewModel(get(), get(), get())
    }
}

val dataModule = module {

    single { Dispatchers.Default }
    single { HealthServicesManager(get()) }

    single<IHealthRepository> {
        HealthRepository(get(), get())
    }
    single<IDocumentsRepository> {
        DocumentsRepository(androidContext())
    }
}
val censorModule = module {
    single {
        HealthServices.getClient(androidContext())
    }
}

val useCaseModule = module {
    factory { HRUseCase(get()) }
    factory { DocumentsUseCase(get(), get()) }
}