package com.harman.wearosapp.domain.use_cases

import com.harman.wearosapp.domain.model.HRRecordModel
import com.harman.wearosapp.domain.repository.IHealthRepository

class HRUseCase(private val repository: IHealthRepository) {
    suspend fun saveHRRecord(hrRecord: HRRecordModel){
        repository.saveHRValueToDb(hrRecord)
    }
    fun receiveHeartRateCensorMeasurement() = repository.getHRCensorFlow()

    fun receiveLatestHeartRateUpdate() = repository.getValueFromDb()


}