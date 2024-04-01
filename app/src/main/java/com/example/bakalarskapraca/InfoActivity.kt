package com.example.bakalarskapraca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val backToMain: ImageButton = findViewById(R.id.backToMainBtn)

        backToMain.setOnClickListener {
            finish()
        }

    }
}