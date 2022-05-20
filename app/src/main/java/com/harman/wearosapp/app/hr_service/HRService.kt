package com.harman.wearosapp.app.hr_service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.wear.ongoing.OngoingActivity
import androidx.wear.ongoing.Status
import com.harman.wearosapp.app.R
import com.harman.wearosapp.app.other.NOTIFICATION_ID
import com.harman.wearosapp.app.other.SENSOR_CHANNEL_ID
import com.harman.wearosapp.app.ui.heart_rate_screen.HeartRateActivity
import com.harman.wearosapp.domain.model.HRRecordModel
import com.harman.wearosapp.domain.use_cases.HRUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent
import java.time.Clock

class HRService : LifecycleService(), KoinComponent {

    private val hrUseCase: HRUseCase by inject()
    private val dispatcher: CoroutineDispatcher by inject()


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()

        (getSystemService(Context.POWER_SERVICE) as PowerManager).addThermalStatusListener {
            Log.d(TAG, "onCreate: $it")
        }
        val onPressIntent = Intent(this, HeartRateActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 1, onPressIntent, PendingIntent.FLAG_IMMUTABLE)
        val name = getString(R.string.hr_service_notification_channel_name)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(SENSOR_CHANNEL_ID, name, importance)
        val manager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
        val notification = NotificationCompat.Builder(this, SENSOR_CHANNEL_ID)
            .setStyle(NotificationCompat.BigTextStyle())
            .setContentTitle(this.getString(R.string.hr_service_notification_tittle))
            .setContentText(getString(R.string.hr_service_notification_description))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)


        val ongoingActivityStatus = Status.Builder()
            .build()

        val ongoingActivity =
            OngoingActivity.Builder(applicationContext, NOTIFICATION_ID, notification)
                .setStaticIcon(R.drawable.ic_heart)
                .setTouchIntent(pendingIntent)
                .setStatus(ongoingActivityStatus)
                .build()

        ongoingActivity.apply(applicationContext)

        startForeground(NOTIFICATION_ID, notification.build())

        lifecycleScope.launchWhenCreated {
            hrUseCase.deletePreviousValue()
        }

        hrUseCase.receiveHeartRateCensorMeasurement().asLiveData().observe(this) {
            lifecycleScope.launch(dispatcher) {
                hrUseCase.saveHRRecord(
                    HRRecordModel(
                        it.last(),
                        Clock.systemUTC().millis()
                    )
                )
            }
        }

    }

    companion object {
        const val TAG = "HRService"
    }
}