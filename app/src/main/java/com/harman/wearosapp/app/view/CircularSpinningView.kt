package com.harman.wearosapp.app.view

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import com.harman.wearosapp.app.R

class CircularSpinningView(ctx: Context, attributeSet: AttributeSet): View(ctx, attributeSet) {


    val isScreenRound = ctx.resources.configuration.isScreenRound
    private var rect: RectF? = null
    private val paint: Paint = Paint()
    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 15f
        paint.color = ContextCompat.getColor(context, R.color.purple_dark)
    }

    private var currentSweepAngle = 0

    fun startSpinnerAnimation(){
        ValueAnimator.ofInt(0, 360).apply {
            duration = 2000
            interpolator = LinearInterpolator()
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener { valueAnimator ->
                currentSweepAngle = valueAnimator.animatedValue as Int
                invalidate()
            }
        }.start()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rect = RectF(
            0f, 0f, w.toFloat(), h.toFloat()
        )
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (rect == null) canvas?.let {
            RectF(it.clipBounds)
        }

        if (isScreenRound)
        rect?.let {
            canvas?.drawArc(
                it, 190f+currentSweepAngle, 120f, false, paint
            )
            canvas?.drawArc(
                it, 10f+currentSweepAngle, 120f, false, paint
            )

        }

    }
}