package com.example.pianoman

import android.content.Context
import android.content.SharedPreferences

//Crédit : Sayandh KP
class SharedPreference(val context: Context) {
    private val PREFS_NAME = "HighScore"
    val sharedPref: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun save(KEY_NAME: String, value: Int) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putInt(KEY_NAME, value)
        editor.apply()
    }

    fun getValueInt(KEY_NAME: String): Int {
        return sharedPref.getInt(KEY_NAME, 0)
    }
}