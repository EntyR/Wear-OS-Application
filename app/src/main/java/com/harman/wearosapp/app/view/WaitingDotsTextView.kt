package com.harman.wearosapp.app.view

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import androidx.appcompat.widget.AppCompatTextView
import java.util.Collections

class WaitingDotsTextView(ctx: Context, attr: AttributeSet?) : AppCompatTextView(ctx, attr) {

    private val animator: ValueAnimator = ValueAnimator.ofInt(0, 4).apply {
        duration = 2000
        interpolator = LinearInterpolator()
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.RESTART


    }
    private val startText = text

    @SuppressLint("SetTextI18n")
    fun startAnimation() {
        animator.addUpdateListener { valueAnimator ->
            val dots = Collections.nCopies(valueAnimator.animatedValue as Int, ".")
                .joinToString(separator = "")
            text = "$startText$dots"
            invalidate()

        }
        animator.start()
    }

    fun stopAnimation() {
        animator.removeAllListeners()
        animator.cancel()
    }

}