package com.groliks.cookingrecipes.view.util

import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.widget.TextView

class TextLoadingAnimator(view: TextView) {
    private lateinit var animator: ValueAnimator

    init {
        val typeEvaluator =
            TypeEvaluator<String> { fraction, startValue, endValue ->
                when {
                    fraction < 0.25 -> startValue
                    fraction < 0.5 -> "$startValue."
                    fraction < 0.75 -> "$startValue.."
                    else -> endValue
                }
            }
        val startText = view.text
        val endText = "$startText..."
        animator =
            ValueAnimator.ofObject(typeEvaluator, startText, endText).apply {
                duration = 2000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.RESTART
                addUpdateListener { updatedAnimation ->
                    view.text = updatedAnimation.animatedValue as String
                }
            }
    }

    fun start() {
        animator.start()
    }

    fun stop() {
        animator.end()
    }
}