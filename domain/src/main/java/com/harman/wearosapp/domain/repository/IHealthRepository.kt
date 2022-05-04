package com.harman.wearosapp.domain.repository

interface IHealthRepository {
    suspend fun saveHRValueToDb(hrRecord: Double)
}