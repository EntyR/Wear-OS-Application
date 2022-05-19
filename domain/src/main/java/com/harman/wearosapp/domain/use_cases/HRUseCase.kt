package com.harman.wearosapp.domain.use_cases

import com.harman.wearosapp.domain.repository.IHealthRepository

class HRUseCase(private val repository: IHealthRepository) {
    suspend fun saveHRRecord(hrRecord: Double){
        repository.saveHRValueToDb(hrRecord)
    }
    fun receiveHeartRateMeasurement() = repository.getHRFlow()
}