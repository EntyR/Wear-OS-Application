package com.harman.wearosapp.app.watchface

import android.content.ComponentName
import android.content.Context
import android.graphics.Color
import android.graphics.RectF
import androidx.wear.watchface.CanvasComplicationFactory
import androidx.wear.watchface.ComplicationSlot
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.complications.ComplicationSlotBounds
import androidx.wear.watchface.complications.DefaultComplicationDataSourcePolicy
import androidx.wear.watchface.complications.SystemDataSources
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.rendering.CanvasComplicationDrawable
import androidx.wear.watchface.complications.rendering.ComplicationDrawable
import androidx.wear.watchface.style.CurrentUserStyleRepository
import com.harman.wearosapp.app.other.*


/**
 * Base config class for all complications in WatchFace.
 * only register top complication slot
 */
sealed class HRComplicationConfig(val id: Int, val supportedTypes: List<ComplicationType>) {
    object Left : HRComplicationConfig(
        COMPLICATION_ID,
        listOf(
            ComplicationType.RANGED_VALUE,
            ComplicationType.MONOCHROMATIC_IMAGE,
            ComplicationType.SHORT_TEXT,
            ComplicationType.SMALL_IMAGE
        )
    )
}

/**
 * Initializes default top complication slots .
 */
fun createComplicationSlotManager(
    context: Context,
    currentUserStyleRepository: CurrentUserStyleRepository,
): ComplicationSlotsManager {


    val complicationDrawable = ComplicationDrawable(context)
    complicationDrawable.activeStyle.backgroundColor = Color.TRANSPARENT
    complicationDrawable.activeStyle.borderColor = Color.TRANSPARENT

    complicationDrawable.activeStyle.textColor = Color.WHITE

    val defaultCanvasComplicationFactory =
        CanvasComplicationFactory { watchState, listener ->
            CanvasComplicationDrawable(
                complicationDrawable,
                watchState,
                listener
            )
        }

    /*
     * Complication is fixed to receive value only from HRComplicationDataSource class
     * with SHORT_TEXT complication Type
     * extend defaultDataSourcePolicy to allow other complication
     */
    val topComplication = ComplicationSlot.createRoundRectComplicationSlotBuilder(
        id = HRComplicationConfig.Left.id,
        canvasComplicationFactory = defaultCanvasComplicationFactory,
        supportedTypes = HRComplicationConfig.Left.supportedTypes,
        defaultDataSourcePolicy = DefaultComplicationDataSourcePolicy(
            ComponentName(context, HRComplicationDataSource::class.java),
            ComplicationType.SHORT_TEXT,
            SystemDataSources.NO_DATA_SOURCE,
            ComplicationType.NO_DATA
        ),
        bounds = ComplicationSlotBounds(
            RectF(
                LEFT_BOUND,
                TOP_BOUNDS,
                RIGHT_BOUND,
                BOTTOM_BOUND
            )
        )
    ).build()


    return ComplicationSlotsManager(
        listOf(topComplication),
        currentUserStyleRepository
    )
}