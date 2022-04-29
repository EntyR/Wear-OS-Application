package com.harman.wearosapp.app.watchface

import android.content.Context
import android.graphics.*
import android.view.SurfaceHolder
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import com.harman.wearosapp.app.R
import com.harman.wearosapp.app.other.getUTCHours
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private const val FRAME_PERIOD_MS_DEFAULT: Long = 16L

/**
 * Render class for Harman WatchFace class
 * TODO migrate to Render2
 */
class HeartRateRenderer(
    private val context: Context,
    surfaceHolder: SurfaceHolder,
    watchState: WatchState,
    currentUserStyleRepository: CurrentUserStyleRepository,
    canvasType: Int
) : Renderer.CanvasRenderer(
    surfaceHolder,
    currentUserStyleRepository,
    watchState,
    canvasType,
    FRAME_PERIOD_MS_DEFAULT
) {

    private val mainPaint = Paint()
    private val colors = intArrayOf(
        Color.BLACK,
        Color.BLACK,
        context.getColor(R.color.purple_primary),
        Color.BLACK,
    )
    private val timeNumericPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.purple_primary)
        textSize = 96f
        typeface = context.resources.getFont(R.font.roboto_thin)
    }
    private val periodPaint = Paint(timeNumericPaint).apply {
        textSize = 40f
        typeface = context.resources.getFont(R.font.roboto_light)
    }
    private val datePaint = Paint(periodPaint).apply {
        textSize = 25f
        textAlign = Paint.Align.RIGHT
    }

    override fun renderHighlightLayer(canvas: Canvas, bounds: Rect, zonedDateTime: ZonedDateTime) {
        canvas.drawColor(renderParameters.highlightLayer!!.backgroundTint)
    }

    private val dateWithoutTimeFormatter = DateTimeFormatter.ofPattern("LLL dd yyyy")
    private var previousTime: ZonedDateTime? = null
    var shaderIsSet = false

    override fun render(canvas: Canvas, bounds: Rect, zonedDateTime: ZonedDateTime) {

        if (!shaderIsSet) {
            val backgroundColor = SweepGradient(
                bounds.exactCenterX(),
                bounds.exactCenterY(),
                colors,
                null
            )
            val matrix = Matrix()
            matrix.preRotate(27f, bounds.exactCenterX(), bounds.exactCenterY())
            backgroundColor.setLocalMatrix(matrix)
            mainPaint.shader = backgroundColor
            canvas.drawPaint(mainPaint)
        }


        if (zonedDateTime != previousTime) {
            val textBound = Rect()
            previousTime = zonedDateTime
            val baseOffset = bounds.exactCenterX() /3
            val (hour, utc) = zonedDateTime.getUTCHours()
            val text = "${hour}:${zonedDateTime.minute}"
            val timeWidth = timeNumericPaint.measureText(text)
            val periodWidth = periodPaint.measureText(utc.stringValue)
            timeNumericPaint.getTextBounds(text, 0, text.length, textBound)
            val height = textBound.height()
            canvas.drawText(
                text,
                baseOffset,
                bounds.exactCenterY() * 1.56f,
                timeNumericPaint
            )
            canvas.drawText(
                utc.stringValue,
                baseOffset + timeWidth,
                bounds.exactCenterY() * 1.56f,
                periodPaint
            )
            canvas.drawText(zonedDateTime.format(dateWithoutTimeFormatter), baseOffset + timeWidth+periodWidth,
                (bounds.exactCenterY()*1.56f+height/2), datePaint)
        }


    }

}
