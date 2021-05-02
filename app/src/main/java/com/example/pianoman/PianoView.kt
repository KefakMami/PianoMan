package com.example.pianoman

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.SparseIntArray
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class PianoView @JvmOverloads constructor (context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0):
        SurfaceView(context, attributes,defStyleAttr), SurfaceHolder.Callback, Runnable {

    var sharedPreference: SharedPreference = SharedPreference(context)
    private lateinit var canvas: Canvas
    private lateinit var thread: Thread
    var screenWidth: Float = 0f
    var screenHeight: Float = 0f
    private val backgroundPaint = Paint()
    private val piano: Piano = Piano(this)
    private var drawing = false
    var score: Score = Score()
    var levelId: Int = 0
    var started = false
    var acc = false

    private val textColor = Paint()
    private val redTextColor = Paint()

    @RequiresApi(Build.VERSION_CODES.O)
    val font = resources.getFont(R.font.arcadeclassic)

    val soundPool: SoundPool
    val soundMap: SparseIntArray
    var mediaPlayer: MediaPlayer? = null

    private val notes = arrayListOf<Note>()

    val activity = context as FragmentActivity

    init {
        backgroundPaint.color = Color.WHITE

        val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()

        soundPool = SoundPool.Builder()
                .setMaxStreams(26)
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
        pauseMusic()
        drawing = false
        thread.join()
    }

    fun resume() {
        if (started) playMusic()
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
        if(!notes.last().noteOnScreen && !acc) {
            drawing = false
            stopMusic()
            showGameEndFragment()
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
        soundPool.play(soundMap[pitch], 1f, 1f, 1, 0, 1f)
    }

    fun stopSound() {
        soundPool.autoPause()
    }

    fun loadNotes(id: Int, spCoeff: Int) {
        val loadArray: Array<String> = resources.getStringArray(id)
        val speed: Float = loadArray[0].toFloat()
        val coeff: Float = when(spCoeff) {
            1 -> 0.75f
            2 -> 1f
            3 -> 1.25f
            else -> 1f
        }
        var pitch: Int
        var duration: Float
        var position = 1f
        for(i in 1 until loadArray.size) {
            pitch = loadArray[i].split(";")[0].toInt()
            duration = loadArray[i].split(";")[1].toFloat()
            if(i == 1) {
                notes.add(FirstNote(speed * coeff, piano, this, pitch, position, duration))
            }
            else notes.add(Note(speed * coeff, piano, this, pitch, position, duration))
            position += duration
        }
        levelId = id
        loadMusic(id)
    }

    // Crédit : Vlad
    private tailrec fun Context?.getActivity(): Activity? = this as? Activity ?: (this as? ContextWrapper)?.baseContext?.getActivity()

    private fun showGameEndFragment() {
        var b: Bundle = Bundle()
        b.putInt("score", score.score)
        b.putFloat("precision", score.precision())
        val fragment: GameEndFragment = GameEndFragment()
        fragment.arguments = b
        val fragmentManager = activity.supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit()
    }

    fun loadMusic(id: Int) {
        if (mediaPlayer == null) {
            acc = true
            mediaPlayer = when(id) {
                R.array.fantaisie_impromptu -> MediaPlayer.create(this.activity, R.raw.fantaisie_lh)
                else -> {
                    acc = false
                    return
                }
            }
            mediaPlayer!!.isLooping = false
        }
        mediaPlayer?.setOnCompletionListener {
            if(acc) showGameEndFragment()
        }
    }

    fun playMusic() {
        if (mediaPlayer != null) mediaPlayer!!.start()
    }

    fun pauseMusic() {
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) mediaPlayer!!.pause()
    }

    fun stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }
}
