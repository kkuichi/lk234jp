package com.example.bakalarskapraca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val settingsButton: ImageButton = findViewById(R.id.settings_btn)
        val teoriaButton: Button = findViewById(R.id.teoria_btn)
        val testyButton: Button = findViewById(R.id.testy_btn)
        val ulohyButton: Button = findViewById(R.id.ulohy_btn)


        settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
            teoriaButton.setOnClickListener {
            startActivity(Intent(this, TeoriaActivity::class.java))
        }
    }
}