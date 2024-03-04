package com.example.bakalarskapraca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        val backToMain: ImageButton = findViewById(R.id.backToMainBtn)
        val switchTheme: Button = findViewById(R.id.theme_switch)


        backToMain.setOnClickListener {
            finish()
        }

        switchTheme.setOnClickListener {
            // Check what the current night mode setting is
            when (AppCompatDelegate.getDefaultNightMode()) {
                AppCompatDelegate.MODE_NIGHT_YES -> {
                    // If it's currently set to 'yes' (night mode), change to 'no' (day mode)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                else -> {
                    // If it's currently set to 'no' (day mode) or 'follow system', change to 'yes' (night mode)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }
            // Recreate the activity for the theme change to take effect
            recreate()
        }
    }
}