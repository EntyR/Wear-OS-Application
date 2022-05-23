package com.harman.wearosapp.domain.repository

import com.harman.wearosapp.domain.model.HRRecordModel
import kotlinx.coroutines.flow.Flow

interface IHealthRepository {
    suspend fun saveHRValueToDb(hrRecord: HRRecordModel)
    fun getHRCensorFlow(): Flow<List<Double>>
    fun getValueFromDb(): Flow<List<HRRecordModel>>
    suspend fun deletePreviousValue()
}