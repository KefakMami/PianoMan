package com.example.pianoman

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class MainActivity: AppCompatActivity() {

    private val levels: ArrayList<Int> = ArrayList()
    private val titles: ArrayList<String> = ArrayList()
    private val images: ArrayList<Int> = ArrayList()
    private val authors: ArrayList<String> = ArrayList()
    private val difficulty: ArrayList<Int> = ArrayList()
    private var select: Int = 0

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadArrays()
    }

    fun onPlay(view: View) {
        val intent = Intent(this@MainActivity, PianoActivity::class.java)
        intent.putExtra("com.example.pianoman.level", levels[select])
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun onLeft(view: View) {
        if(select > 0) {
            select -= 1
            updateLevel(select)
            if(select == 0) {
                arrowLeft.setImageResource(R.drawable.arrow_hasnonext_l)
            }
            arrowRight.setImageResource(R.drawable.arrow_hasnext_r)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun onRight(view: View) {
        if(select < levels.size - 1) {
            select += 1
            updateLevel(select)
            if(select == levels.size - 1) {
                arrowRight.setImageResource(R.drawable.arrow_hasnonext_r)
            }
            else arrowLeft.setImageResource(R.drawable.arrow_hasnext_l)
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun loadArrays() {
        levels.add(R.array.Scale)
        titles.add("Scale")
        images.add(R.drawable.piano)
        authors.add("Piano Man")
        difficulty.add(1)

        levels.add(R.array.AuClairDeLaLune)
        titles.add("Au Clair de la Lune")
        images.add(R.drawable.clairdelune)
        authors.add("Lully")
        difficulty.add(1)

        levels.add(R.array.Megalovania)
        titles.add("Megalovania")
        images.add(R.drawable.sans)
        authors.add("Toby Fox")
        difficulty.add(2)

        levels.add(R.array.fantaisie_impromptu)
        titles.add("Fantaisie Impromptu")
        images.add(R.drawable.chopin)
        authors.add("Frederic Chopin")
        difficulty.add(3)

        levelTitle.text = titles[0]
        centerImage.setImageResource(images[0])
        levelDifficulty.text = getText(R.string.easy)
        levelDifficulty.setTextColor(getColor(R.color.green))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun updateLevel(selector: Int) {
        levelTitle.text = titles[selector]
        centerImage.setImageResource(images[selector])
        levelComposer.text = authors[selector]

        when(difficulty[selector]) {
            1 -> {
                levelDifficulty.text = getText(R.string.easy)
                levelDifficulty.setTextColor(getColor(R.color.green))
            }
            2 -> {
                levelDifficulty.text = getText(R.string.medium)
                levelDifficulty.setTextColor(getColor(R.color.orange))
            }
            3 -> {
                levelDifficulty.text = getText(R.string.hard)
                levelDifficulty.setTextColor(getColor(R.color.red))
            }
        }
    }
}