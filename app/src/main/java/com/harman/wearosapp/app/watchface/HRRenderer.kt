package com.harman.wearosapp.app.watchface
import android.content.Context
import android.graphics.*
import android.view.SurfaceHolder
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import com.harman.wearosapp.app.R
import java.time.ZonedDateTime

private const val FRAME_PERIOD_MS_DEFAULT: Long = 16L

class HeartRateRenderer(
    private val context: Context,
    surfaceHolder: SurfaceHolder,
    watchState: WatchState,
    private val complicationSlotsManager: ComplicationSlotsManager,
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
        textSize = 70f
        typeface = context.resources.getFont(R.font.roboto_thin)
    }
    private val periodPaint = Paint(timeNumericPaint).apply {
        textSize = 40f
    }

    var shaderIsSet = false



    override fun renderHighlightLayer(canvas: Canvas, bounds: Rect, zonedDateTime: ZonedDateTime) {
        canvas.drawColor(renderParameters.highlightLayer!!.backgroundTint)
        complicationSlotsManager.complicationSlots.forEach { (_, complication) ->
            if (complication.enabled) {
                complication.renderHighlightLayer(canvas, zonedDateTime, renderParameters)
            }
        }
    }



    //TODO display time in utc
    override fun render(canvas: Canvas, bounds: Rect, zonedDateTime: ZonedDateTime) {
        if(!shaderIsSet) {
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
        }


        canvas.drawPaint(mainPaint)
        val width = timeNumericPaint.measureText("${zonedDateTime.hour}:${zonedDateTime.minute}")
        canvas.drawText("${zonedDateTime.hour}:${zonedDateTime.minute}", bounds.exactCenterX()/4, bounds.exactCenterY()*1.56f, timeNumericPaint)
        canvas.drawText("AM", bounds.exactCenterX()/4 + width, bounds.exactCenterY()*1.56f, periodPaint)
        complicationSlotsManager.complicationSlots.forEach { (_, complication) ->
            if (complication.enabled) {
                complication.render(canvas, zonedDateTime, renderParameters)
            }
        }

    }

}
