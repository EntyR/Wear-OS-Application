package com.harman.wearosapp.data.repository

import com.harman.wearosapp.data.data_source.HealthServicesManager
import com.harman.wearosapp.domain.repository.IHealthRepository
import kotlinx.coroutines.flow.map

class HealthRepository(val dataSource: HealthServicesManager) : IHealthRepository {
    //TODO Save value to database
    override suspend fun saveHRValueToDb(hrRecord: Double) {

    }

    override fun getHRFlow() =
        dataSource.heartRateMeasureFlow().map {
            it.map { data ->
                data.value.asDouble()
            }
        }

}