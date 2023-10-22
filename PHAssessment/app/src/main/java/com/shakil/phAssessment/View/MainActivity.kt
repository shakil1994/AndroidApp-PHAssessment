package com.shakil.phAssessment.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import com.shakil.phAssessment.Common.Constants
import com.shakil.phAssessment.Common.SharedPref
import com.shakil.phAssessment.R
import com.shakil.phAssessment.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding

    private var preferenceManager: SharedPref? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        preferenceManager = SharedPref(this)

        binding.apply {
            this!!.btnStart.setOnClickListener {
                startActivity(Intent(this@MainActivity, QuizAnsActivity::class.java))
                finish()
            }
        }

        val highScoreFromPreferences = preferenceManager?.getInt(Constants.HIGHSCORE) ?: 0
        Log.e("MAINACTIVITY", "SCORE: " + highScoreFromPreferences)

        /** Set the High Score value in TextView **/
        binding!!.txtScoreValue.text = StringBuilder().append(highScoreFromPreferences).append(" Point")
    }
}