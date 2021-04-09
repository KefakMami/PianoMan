package com.example.pianoman

import android.graphics.Point
import android.graphics.RectF
import android.view.MotionEvent
import androidx.core.graphics.contains

class PianoKey (val rect: RectF, val pitch: Int) {

    init {

    }

    var isPressed: Boolean = false

}