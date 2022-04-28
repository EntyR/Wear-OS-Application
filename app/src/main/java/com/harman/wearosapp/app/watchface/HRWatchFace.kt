package com.harman.wearosapp.app.watchface

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.view.SurfaceHolder
import androidx.wear.watchface.*
import androidx.wear.watchface.style.CurrentUserStyleRepository
import com.harman.wearosapp.app.MainActivity


class HRWatchFace : WatchFaceService() {

    override fun createComplicationSlotsManager(currentUserStyleRepository: CurrentUserStyleRepository): ComplicationSlotsManager {
        return createComplicationSlotManager(context = this, currentUserStyleRepository)
    }


    override suspend fun createWatchFace(
        surfaceHolder: SurfaceHolder,
        watchState: WatchState,
        complicationSlotsManager: ComplicationSlotsManager,
        currentUserStyleRepository: CurrentUserStyleRepository
    ): WatchFace = WatchFace(
        WatchFaceType.DIGITAL,
        HeartRateRenderer(
            this,
            surfaceHolder,
            watchState,
            complicationSlotsManager,
            currentUserStyleRepository,
            CanvasType.HARDWARE
        )
    ).setTapListener(object : WatchFace.TapListener{
        override fun onTapEvent(
            tapType: Int,
            tapEvent: TapEvent,
            complicationSlot: ComplicationSlot?
        ) {
            if (tapType != TapType.CANCEL && tapType != TapType.DOWN){
                val intent = Intent(
                    this@HRWatchFace, MainActivity::class.java
                ).setFlags(FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }

        }
    })
}