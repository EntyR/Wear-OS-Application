package com.harman.wearosapp.app.hr_service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.PassiveMonitoringUpdate
import com.harman.wearosapp.domain.use_cases.HRUseCase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class HRBroadcastReceiver() : BroadcastReceiver(
), KoinComponent {

    private val hrUseCase: HRUseCase = get()

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive: Received")
        val lastDataPoint = (PassiveMonitoringUpdate.fromIntent(intent) ?: return)
            .dataPoints
            .filter { it.dataType == DataType.HEART_RATE_BPM }
            .filter {
                it.value.asDouble() > 0
            }
            .maxByOrNull { it.endDurationFromBoot } ?: return
        GlobalScope.launch {
            hrUseCase.saveHRRecord(lastDataPoint.value.asDouble())
        }
    }
    companion object {
        const val TAG = "HRBroadcastReceiver"
    }
}