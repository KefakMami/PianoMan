package com.example.pianoman

import android.media.SoundPool
import android.media.MediaPlayer
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi


class FirstNote(speed: Float, piano: Piano, view: PianoView, pitch: Int, position: Float, duree: Float): Note(speed, piano, view, pitch, position, duree) {
    //var mediaPlayer: MediaPlayer? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun play() {
        view.playMusic()
    }

    override fun update(interval: Double) {
        if (noteOnScreen) {
            rect.top += (interval * speed).toFloat()
            rect.bottom += (interval * speed).toFloat()
        }
        if (rect.top > piano.pianoTop && noteOnScreen) {
            play()
            view.started = true
            deleteNote()
        }
    }
}