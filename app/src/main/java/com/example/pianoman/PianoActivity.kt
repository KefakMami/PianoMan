package com.example.pianoman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import android.os.Build
import android.content.Intent
import android.content.Intent.getIntent
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_piano.*


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class PianoActivity : AppCompatActivity() {
    lateinit var pianoView: PianoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val level: Int = intent.getIntExtra("com.example.pianoman.level", R.array.Scale)

        setContentView(R.layout.activity_piano)
        pianoView = findViewById(R.id.vMain)
        pianoView.loadNotes(level)
        while (pianoView.gameOver) {
            gameEnd()
        }
    }

    override fun onPause() {
        super.onPause()
        pianoView.pause()
    }

    override fun onResume() {
        super.onResume()
        pianoView.resume()
    }

    fun gameEnd() {
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        val gameEndFragment = GameEndFragment()
        ft.show(gameEndFragment)
    }

    override fun onBackPressed() {
        Toast.makeText(this, "bruh", Toast.LENGTH_SHORT).show()
        if (pianoView.gameOver) {
            gameEndFrame.visibility = View.VISIBLE
        }
        //super.onBackPressed()
    }
}