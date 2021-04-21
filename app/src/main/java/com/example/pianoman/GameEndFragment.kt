package com.example.pianoman

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
        val scoreText = view.scoreInt
        val precisionText = view.precisionFloat
        scoreText.text = args?.getInt("score").toString()
        precisionText.text = args?.getFloat("precision").toString() + " %"
//        when(args?.getFloat("precision")) {
//
//            else -> "F"
//        }

        val activity = activity
        //setScoreText()
        return view
    }

}