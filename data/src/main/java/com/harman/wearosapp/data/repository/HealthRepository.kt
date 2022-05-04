package com.harman.wearosapp.data.repository

import android.util.Log
import com.harman.wearosapp.domain.repository.IHealthRepository

class HealthRepository: IHealthRepository {
    //TODO Save value to database
    override suspend fun saveHRValueToDb(hrRecord: Double) {
        Log.d(TAG, "Record: $hrRecord")
    }

    companion object {
        const val TAG = "HealthRepository"
    }
}