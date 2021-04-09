package com.example.pianoman

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

    private val dm: DisplayMetrics = DisplayMetrics()
    lateinit var pianoView: PianoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        windowManager.defaultDisplay.getMetrics(dm)
        val activityWidth = dm.widthPixels
        val activityHeight = dm.heightPixels
        setContentView(R.layout.activity_main)
        pianoView = findViewById(R.id.vMain)
        Toast.makeText(this, activityHeight.toString() + " " + activityWidth.toString(), Toast.LENGTH_SHORT).show()
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