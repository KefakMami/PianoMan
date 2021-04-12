package com.example.pianoman

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.SparseIntArray
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_piano.view.*
import androidx.fragment.app.FragmentManager

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class PianoView @JvmOverloads constructor (context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0):
        SurfaceView(context, attributes,defStyleAttr), SurfaceHolder.Callback, Runnable {


    private lateinit var canvas: Canvas
    private lateinit var thread: Thread
    var screenWidth: Float = 0f
    var screenHeight: Float = 0f
    private val backgroundPaint = Paint()
    private val piano: Piano = Piano(this)
    private var drawing = false
    var score: Score = Score()

    private val textColor = Paint()
    private val redTextColor = Paint()

    @RequiresApi(Build.VERSION_CODES.O)
    val font = resources.getFont(R.font.arcadeclassic)

    private val soundPool: SoundPool
    private val soundMap: SparseIntArray

    private val notes = arrayListOf<Note>()

    var gameOver: Boolean = false

    val activity = context as FragmentActivity

    init {
        backgroundPaint.color = Color.WHITE

        val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()

        soundPool = SoundPool.Builder()
                .setMaxStreams(25)
                .setAudioAttributes(audioAttributes)
                .build()

        soundMap = SparseIntArray(25)
        soundMap.put(0, soundPool.load(context, R.raw.c4, 1))
        soundMap.put(1, soundPool.load(context, R.raw.cd4, 1))
        soundMap.put(2, soundPool.load(context, R.raw.d4, 1))
        soundMap.put(3, soundPool.load(context, R.raw.dd4, 1))
        soundMap.put(4, soundPool.load(context, R.raw.e4, 1))
        soundMap.put(5, soundPool.load(context, R.raw.f4, 1))
        soundMap.put(6, soundPool.load(context, R.raw.fd4, 1))
        soundMap.put(7, soundPool.load(context, R.raw.g4, 1))
        soundMap.put(8, soundPool.load(context, R.raw.gd4, 1))
        soundMap.put(9, soundPool.load(context, R.raw.a4, 1))
        soundMap.put(10, soundPool.load(context, R.raw.ad4, 1))
        soundMap.put(11, soundPool.load(context, R.raw.b4, 1))
        soundMap.put(12, soundPool.load(context, R.raw.c5, 1))
        soundMap.put(13, soundPool.load(context, R.raw.cd5, 1))
        soundMap.put(14, soundPool.load(context, R.raw.d5, 1))
        soundMap.put(15, soundPool.load(context, R.raw.dd5, 1))
        soundMap.put(16, soundPool.load(context, R.raw.e5, 1))
        soundMap.put(17, soundPool.load(context, R.raw.f5, 1))
        soundMap.put(18, soundPool.load(context, R.raw.fd5, 1))
        soundMap.put(19, soundPool.load(context, R.raw.g5, 1))
        soundMap.put(20, soundPool.load(context, R.raw.gd5, 1))
        soundMap.put(21, soundPool.load(context, R.raw.a5, 1))
        soundMap.put(22, soundPool.load(context, R.raw.ad5, 1))
        soundMap.put(23, soundPool.load(context, R.raw.b5, 1))
        soundMap.put(24, soundPool.load(context, R.raw.c6, 1))

        textColor.color = Color.BLACK
        textColor.textSize = 90f
        redTextColor.color = Color.RED
        redTextColor.textSize = 90f
        textColor.typeface = font
        redTextColor.typeface = font
    }

    fun pause() {
        drawing = false
        thread.join()
    }

    fun resume() {
        drawing = true
        thread = Thread(this)
        thread.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceCreated(holder: SurfaceHolder) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {}

    override fun run() {
        var previousFrameTime = System.currentTimeMillis()
        while (drawing) {
            val currentTime = System.currentTimeMillis()
            val elapsedTimeMS = (currentTime - previousFrameTime).toDouble()
            updatePositions(elapsedTimeMS)
            draw()
            previousFrameTime = currentTime
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        screenHeight = h.toFloat()
        screenWidth = w.toFloat()

        piano.width = w.toFloat()
        piano.height = h.toFloat()
        piano.pianoTop = 3*h/4f

        piano.whiteKeyWidth = piano.width/piano.nTouchesBlanches

        piano.setPiano()
        for(note in notes) {
            note.setNote()
        }
    }

    private fun draw() {
        if(holder.surface.isValid) {
            canvas = holder.lockCanvas()
            canvas.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), backgroundPaint)
            for(note in notes) note.draw(canvas)
            piano.draw(canvas)
            canvas.drawText("Score : " + score.score, 30f, 100f, textColor)
            canvas.drawText("Combo : x" + score.multiplier, 30f, 200f, redTextColor)
            holder.unlockCanvasAndPost(canvas)
        }
    }

    private fun updatePositions(elapsedTimeMS: Double) {
        val interval = elapsedTimeMS / 1000.0
        for(note in notes) note.update(interval)
        if(!notes.last().noteOnScreen) {
            //showGameOverDialog(score.score, score.precision())
            endGame()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action = event?.action
        val pressedPitch: Int
        if(action == MotionEvent.ACTION_DOWN) {
            pressedPitch = piano.pressKey(event)
            for (note in notes) {
                note.detectPlay(pressedPitch)
            }
        }
//        if(action == MotionEvent.ACTION_MOVE) {
//            piano.glissandoKey(event)
//        }
        if(action == MotionEvent.ACTION_UP) {
            piano.releaseKey(event)
        }
        return true
    }

    fun playSound(pitch: Int) {
        soundPool.play(soundMap.get(pitch), 1f, 1f, 1, 0, 1f)
    }

    fun stopSound() {
        soundPool.autoPause()
    }

    fun loadNotes(id: Int) {
        val loadArray: Array<String> = resources.getStringArray(id)
        val speed: Float = loadArray[0].toFloat()
        var pitch: Int
        var duration: Int
        var position = 1
        for(i in 1 until loadArray.size) {
            pitch = loadArray[i].split(";")[0].toInt()
            duration = loadArray[i].split(";")[1].toInt()
            notes.add(Note(speed, piano, this, pitch, position, duration))
            position += duration
        }
    }

    fun endGame() {
        gameOver = true
        //this.thread.interrupt()
    }

//    fun showGameOverDialog(score: Int, precision: Float) {
//        class GameResult: DialogFragment() {
//            override fun onCreateDialog(bundle: Bundle?): Dialog {
//                val builder = AlertDialog.Builder(activity)
//                builder.setTitle("Partie terminée")
//                builder.setMessage("Score : $score \nPrécision : $precision")
//                builder.setPositiveButton("Retour au menu principal", DialogInterface.OnClickListener { _, _->endGame()})
//                return builder.create()
//            }
//        }
//        activity.runOnUiThread(
//                Runnable {
//                    val ft = activity.supportFragmentManager.beginTransaction()
//                    val prev =
//                            activity.supportFragmentManager.findFragmentByTag("dialog")
//                    if (prev != null) {
//                        ft.remove(prev)
//                    }
//                    ft.addToBackStack(null)
//                    val gameResult = GameResult()
//                    gameResult.isCancelable = false
//                    gameResult.show(ft,"dialog")
//                }
//        )
//    }
}
