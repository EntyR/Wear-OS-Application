package com.harman.wearosapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface IHealthRepository {
    suspend fun saveHRValueToDb(hrRecord: Double)
    fun getHRFlow(): Flow<List<Double>>

}