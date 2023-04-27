package com.yosuahaloho.storiku.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.animation.Animation

/**
 * Created by Yosua on 26/04/2023
 */

fun View.makeAnimation(animation: Animation, onAnimationEnd: () -> Unit) {
    animation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {}

        override fun onAnimationEnd(animation: Animation?) {
            onAnimationEnd()
        }

        override fun onAnimationRepeat(animation: Animation?) {}
    })

    this.startAnimation(animation)
}

fun View.makeAnimationExplo(animationEnd: () -> Unit) = object : AnimatorListenerAdapter() {
    override fun onAnimationEnd(animation: Animator) {
        animationEnd()
    }
}