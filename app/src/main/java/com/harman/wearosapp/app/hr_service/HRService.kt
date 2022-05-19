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
import androidx.wear.ongoing.OngoingActivity
import androidx.wear.ongoing.Status
import com.harman.wearosapp.app.R
import com.harman.wearosapp.app.other.NOTIFICATION_ID
import com.harman.wearosapp.app.other.SENSOR_CHANNEL_ID
import com.harman.wearosapp.app.ui.heart_rate_screen.HeartRateActivity
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent

class HRService : LifecycleService(), KoinComponent {

    private val healthServicesManager: HealthServicesManager by inject()

    lateinit var  wakeLock: PowerManager.WakeLock


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        wakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag").apply {
                    acquire()
                }
            }

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

        healthServicesManager.heartRateMeasureFlow().asLiveData().observe(this) {

            //TODO save records into db
            val lastHRRecord = it.last().value.asDouble()
            Log.d(TAG, "onCreate: $lastHRRecord")
        }

    }

    override fun onDestroy() {
        wakeLock.release()
        super.onDestroy()
    }
    companion object {
        const val TAG = "HRService"
    }
}