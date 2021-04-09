package com.example.pianoman

import android.graphics.*
import android.os.Build
import android.view.MotionEvent
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.pianoman.PianoKey
import com.example.pianoman.PianoView
import com.example.pianoman.MainActivity

class Piano (val view: PianoView){

    var width: Float = view.screenWidth.toFloat()
    var height: Float = view.screenHeight.toFloat()
    private val nTouchesBlanches = 15
    private val nTouchesNoires = 10
    val whiteKeyWidth = width / nTouchesBlanches
    val blackKeyWidth = 2 * whiteKeyWidth / 3
    val pianoTop: Float = 1100f
    private val blackKeyHeight = 1*(height - pianoTop)/2

    var whiteKeys: ArrayList<PianoKey> = arrayListOf()
    var blackKeys: ArrayList<PianoKey> = arrayListOf()

    private val black = Paint()
    private val white = Paint()
    private val blackLine = Paint()
    private val grey = Paint()
    private val dgrey = Paint()

    init {
        black.color = Color.BLACK
        blackLine.strokeWidth = 8f
        white.color = Color.WHITE
        grey.color = Color.GRAY
        dgrey.color = Color.DKGRAY
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun pressKey(event: MotionEvent): Int{
        var blackKeyPressed: Boolean = false
        var pitch: Int = 0
        for(key in blackKeys) {
            if(key.rect.contains(event.x, event.y)) {
                key.isPressed = true
                blackKeyPressed = true
                view.playSound(key.pitch - 1)
                pitch = key.pitch
            }
            else key.isPressed = false
        }
        for(key in whiteKeys) {
            if (key.rect.contains(event.x, event.y) && !blackKeyPressed) {
                key.isPressed = true
                view.playSound(key.pitch - 1)
                pitch = key.pitch
            }
            else {
                key.isPressed = false
            }
        }
        return pitch
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun glissandoKey(event: MotionEvent) {
        var blackKeyPressed: Boolean = false
        for(key in blackKeys) {
            if(key.rect.contains(event.x, event.y) && !key.isPressed) {
                key.isPressed = true
                blackKeyPressed = true
                view.playSound(key.pitch - 1)
            }
        }
        for(key in whiteKeys) {
            if (key.rect.contains(event.x, event.y) && !blackKeyPressed && !key.isPressed) {
                key.isPressed = true
                view.playSound(key.pitch - 1)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun releaseKey(event: MotionEvent) {
        var blackKeyReleased: Boolean = false
        for(key in blackKeys) {
            if(key.rect.contains(event.x, event.y) && key.isPressed) {
                key.isPressed = false
                blackKeyReleased = true
                // trouver un moyen de couper le son quand on leve le doigt de la touche, pour l'instant le stopSound coupe tout
                //view.stopSound(key.pitch - 1)
            }
        }
        for(key in whiteKeys) {
            if(key.rect.contains(event.x, event.y) && !blackKeyReleased && key.isPressed) {
                key.isPressed = false
                //view.stopSound(key.pitch - 1)
            }
        }
    }

    fun draw(canvas: Canvas) {
        for (key in whiteKeys) {
            if (key.isPressed) canvas.drawRect(key.rect, grey)
            else canvas.drawRect(key.rect, white)
        }

        for (i in 0..nTouchesBlanches) {
            canvas.drawLine(i * whiteKeyWidth, pianoTop, i * whiteKeyWidth, height, blackLine)
        }

        for (key in blackKeys) {
            if (key.isPressed) canvas.drawRoundRect(key.rect, 15f, 15f, dgrey)
            else canvas.drawRoundRect(key.rect, 15f, 15f, black)
        }
        canvas.drawLine(0f, pianoTop, width, pianoTop, blackLine)
    }

    fun setPiano() {
        var pitchCount: Int = 1
        for (i in 0..nTouchesBlanches) {
            val r = RectF(i * whiteKeyWidth, pianoTop, (i+1) * whiteKeyWidth, height)
            whiteKeys.add(PianoKey(r, pitchCount))
            ++pitchCount
            if(i != 2 && i != 6 && i != 9 && i != 13) {
                val rr = RectF((i+1)*whiteKeyWidth - blackKeyWidth / 2, pianoTop, (i+1)*whiteKeyWidth + blackKeyWidth / 2, pianoTop + blackKeyHeight)
                blackKeys.add(PianoKey(rr, pitchCount))
                ++pitchCount
            }
        }
    }
}