package com.example.pianoman

import android.graphics.*
import java.util.*
import android.graphics.RectF
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.graphics.contains
import kotlin.random.Random.Default.nextInt

class Note (val speed: Float, val piano: Piano, val view: PianoView, val pitch: Int, position: Int, val duree: Int){

    val epaisseur: Float = 50f
    var x1: Float = 0f
    var x2: Float = 0f
    val longueur = 150f
    var y: Float = - (longueur + 10f) * position

    val randomColor = Paint()
    val random = Random()
    var noteOnScreen = true

    var rect: RectF

    init {
        randomColor.color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256))

        x1 = when(pitch) {
            in 1..5 -> (pitch * piano.whiteKeyWidth - epaisseur) / 2
            in 6..12 -> ((pitch + 1) * piano.whiteKeyWidth - epaisseur) / 2
            in 13..17 -> ((pitch + 2) * piano.whiteKeyWidth - epaisseur) / 2
            in 18..24 -> ((pitch + 3) * piano.whiteKeyWidth - epaisseur) / 2
            25 -> ((pitch + 4) * piano.whiteKeyWidth - epaisseur) / 2
            else -> (piano.whiteKeyWidth - epaisseur) / 2
        }

        x2 = x1 + epaisseur
        rect = RectF(x1, y, x2, (y - longueur * duree))
    }

    fun draw(canvas: Canvas) {
        if (rect.top > -10 && noteOnScreen) canvas.drawRoundRect(rect, 10f, 10f, randomColor)
    }

    fun update(interval: Double) {
        if (noteOnScreen) {
            rect.top += (interval * speed).toFloat()
            rect.bottom += (interval * speed).toFloat()
        }
        if (rect.top - longueur * duree > piano.pianoTop) {
            view.score.resetMultiplier()
            deleteNote()
        }
    }

    fun deleteNote() {
        noteOnScreen = false
    }

    fun detectPlay(playedPitch: Int) {
        if(rect.top >= piano.pianoTop - 50f && rect.bottom <= piano.pianoTop + 50f && noteOnScreen) {
            if (playedPitch == pitch) {
                view.score.increaseScore((longueur*duree).toInt() - kotlin.math.abs(piano.pianoTop - rect.top).toInt())
                deleteNote()
                view.score.increaseMultiplier()
            }
            else (view.score.resetMultiplier())
        }
    }
}
