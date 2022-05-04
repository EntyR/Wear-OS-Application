package com.harman.wearosapp.app.hr_service

import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.concurrent.futures.await
import androidx.health.services.client.HealthServicesClient
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.PassiveMonitoringConfig

class HealthServicesManager(
    private val context: Context,
    healthServicesClient: HealthServicesClient
) {
    private val passiveMonitoringClient = healthServicesClient.passiveMonitoringClient

    suspend fun hasHeartRateCapability(): Boolean {
        val capabilities = passiveMonitoringClient.capabilities.await()
        return (DataType.HEART_RATE_BPM in capabilities.supportedDataTypesPassiveMonitoring)
    }

    suspend fun registerForHeartRateData() {
        Log.i(TAG, "Registering for background data.")
        val componentName = ComponentName(context, HRBroadcastReceiver::class.java)
        val config = PassiveMonitoringConfig.builder()
            .setDataTypes(setOf(DataType.HEART_RATE_BPM))
            .setComponentName(componentName)
            .build()
        passiveMonitoringClient.registerDataCallback(config).await()
    }

    suspend fun unregisterForHeartRateData() {
        Log.i(TAG, "Unregistering for background data.")
        passiveMonitoringClient.unregisterDataCallback().await()
    }

    companion object {
        const val TAG = "HealthServicesManager"
    }
}