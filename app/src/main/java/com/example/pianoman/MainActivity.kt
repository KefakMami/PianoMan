package com.example.pianoman

import android.content.Intent
import android.graphics.Point
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class MainActivity: AppCompatActivity() {

    lateinit var pianoView: PianoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        pianoView = findViewById(R.id.vMain)

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