package com.example.pianoman

import android.content.Context
import android.content.SharedPreferences

//Crédit : Sayandh KP
class SharedPreference(context: Context) {
    private val name = "HighScore"
    private val sharedPref: SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    fun save(KEY_NAME: String, value: Int) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putInt(KEY_NAME, value)
        editor.apply()
    }

    fun getValueInt(KEY_NAME: String): Int {
        return sharedPref.getInt(KEY_NAME, 0)
    }
}