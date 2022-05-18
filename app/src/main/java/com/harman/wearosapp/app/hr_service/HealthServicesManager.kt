package com.harman.wearosapp.app.hr_service

import android.content.Context
import android.util.Log
import androidx.health.services.client.HealthServicesClient
import androidx.health.services.client.MeasureCallback
import androidx.health.services.client.data.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow

class HealthServicesManager(
    private val healthServicesClient: HealthServicesClient
) {

    fun heartRateMeasureFlow() = callbackFlow {
        val callback = object : MeasureCallback {
            override fun onAvailabilityChanged(dataType: DataType, availability: Availability) =
                Unit

            override fun onData(data: List<DataPoint>) {
                trySendBlocking(data)
            }
        }

        Log.d(TAG, "Registering for data")
        healthServicesClient.measureClient.registerCallback(DataType.HEART_RATE_BPM, callback)

        awaitClose {
            Log.d(TAG, "Unregistering for data")
            healthServicesClient.measureClient.unregisterCallback(DataType.HEART_RATE_BPM, callback)
        }
    }


    companion object {
        const val TAG = "HealthServicesManager"
    }
}