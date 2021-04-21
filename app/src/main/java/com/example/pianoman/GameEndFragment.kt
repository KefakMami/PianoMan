package com.example.pianoman

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import kotlinx.android.synthetic.main.fragment_game_end.*
import kotlinx.android.synthetic.main.fragment_game_end.view.*


class GameEndFragment() : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_game_end, container, false)

        val args = this.arguments
        val precision: Float? = args?.getFloat("precision")
        val scoreText = view.scoreInt
        val precisionText = view.precisionFloat
        val gradeLetterText = view.GradeLetter
        gradeLetterText.text = 2.toString()
        scoreText.text = args?.getInt("score").toString()
        precisionText.text = precision.toString() + " %"
        if (precision != null) {
            if (precision < 50f) {
                gradeLetterText.text = getString(R.string.F)
                gradeLetterText.setTextColor(resources.getColor(R.color.red))
            }
            if (50 <= precision && precision < 60) {
                gradeLetterText.text = getString(R.string.E)
                gradeLetterText.setTextColor(resources.getColor(R.color.orange))
            }
            if (60 <= precision && precision < 70) {
                gradeLetterText.text = getString(R.string.D)
                gradeLetterText.setTextColor(resources.getColor(R.color.yellow))
            }
            if (70 <= precision && precision < 80) {
                gradeLetterText.text = getString(R.string.C)
                gradeLetterText.setTextColor(resources.getColor(R.color.lgreen))
            }
            if (80 <= precision && precision < 90) {
                gradeLetterText.text = getString(R.string.B)
                gradeLetterText.setTextColor(resources.getColor(R.color.dgreen))
            }
            if (90 <= precision && precision < 95) {
                gradeLetterText.text = getString(R.string.A)
                gradeLetterText.setTextColor(resources.getColor(R.color.blue))
            }
            if (95 <= precision && precision <= 100) {
                gradeLetterText.text = getString(R.string.S)
                gradeLetterText.setTextColor(resources.getColor(R.color.purple_500))
            }
        }
        val activity = activity
        //setScoreText()
        return view
    }

}