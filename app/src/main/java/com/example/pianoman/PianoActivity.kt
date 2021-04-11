package com.example.pianoman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import android.os.Build
import android.content.Intent
import android.content.Intent.getIntent


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class PianoActivity : AppCompatActivity() {
    lateinit var pianoView: PianoView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val level: Int = intent.getIntExtra("com.example.pianoman.level", R.array.Scale)

        setContentView(R.layout.activity_piano)
        pianoView = findViewById(R.id.vMain)
        pianoView.loadNotes(level)
    }

    override fun onPause() {
        super.onPause()
        pianoView.pause()
    }

    override fun onResume() {
        super.onResume()
        pianoView.resume()
    }
}