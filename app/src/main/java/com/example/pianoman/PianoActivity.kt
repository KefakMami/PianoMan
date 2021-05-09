package com.example.pianoman

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class PianoActivity : AppCompatActivity() {
    lateinit var pianoView: PianoView
    private var level: Int = 0
    private var speed: Int = 0
    private var highScore: Int = 0
    var isPaused = false
    private val returnFragment: ReturnFragment = ReturnFragment()
    private val fragmentManager = this.supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreference: SharedPreference = SharedPreference(this)

        level = intent.getIntExtra("com.example.pianoman.level", R.array.Scale)
        speed = intent.getIntExtra("com.example.pianoman.speed", 1)
        highScore = sharedPreference.getValueInt(level.toString())

        setContentView(R.layout.activity_piano)
        pianoView = findViewById(R.id.vMain)
        pianoView.loadNotes(level, speed)
    }

    override fun onPause() {
        super.onPause()
        pianoView.pause()
    }

    override fun onResume() {
        super.onResume()
        pianoView.resume()
    }


    override fun onBackPressed() {
        isPaused = when(isPaused) {
            true -> {
                pianoView.resume()
                fragmentManager.beginTransaction().remove(returnFragment).commit()
                //Toast.makeText(this, "Resuming...", Toast.LENGTH_SHORT).show()
                false
            }
            false -> {
                pianoView.pause()
                fragmentManager.beginTransaction().replace(R.id.returnFrame, returnFragment).commit()
                //Toast.makeText(this, "Pausing...", Toast.LENGTH_SHORT).show()
                true
            }
        }
    }


    fun onFinish(view: View) {
        pianoView.stopMusic()

        val returnIntent: Intent = Intent()

        if(pianoView.score.score > highScore) {
            pianoView.sharedPreference.save(level.toString(), pianoView.score.score)
            setResult(1, returnIntent)
        }
        else setResult(0, returnIntent)

        this.finish()
    }

    fun onReset(view: View) {
        val intent = intent
        finish()
        startActivity(intent)
    }

    fun onContinue(view: View) {
        fragmentManager.beginTransaction().remove(returnFragment).commit()
        pianoView.resume()
        isPaused = false
    }

    fun onPauseBtn(view: View) {
        isPaused = when(isPaused) {
            true -> {
                pianoView.resume()
                fragmentManager.beginTransaction().remove(returnFragment).commit()
                //Toast.makeText(this, "Resuming...", Toast.LENGTH_SHORT).show()
                false
            }
            false -> {
                pianoView.pause()
                fragmentManager.beginTransaction().replace(R.id.returnFrame, returnFragment).commit()
                //Toast.makeText(this, "Pausing...", Toast.LENGTH_SHORT).show()
                true
            }
        }
    }
}