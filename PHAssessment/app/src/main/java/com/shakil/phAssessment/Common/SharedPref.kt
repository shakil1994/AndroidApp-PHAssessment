package com.shakil.phAssessment.Common

import android.content.Context
import android.content.SharedPreferences

class SharedPref(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    fun saveInt(key: Int, value: Int) {
        sharedPreferences.edit().putInt(key.toString(), value).apply()
    }

    fun getInt(key: Int): Int {
        return sharedPreferences.getInt(key.toString(), 0)
    }
}