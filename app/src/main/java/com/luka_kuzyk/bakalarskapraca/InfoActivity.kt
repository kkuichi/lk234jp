package com.luka_kuzyk.bakalarskapraca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        // Nájdenie tlačidla pre návrat na hlavnú obrazovku Main
        val backToMain: ImageButton = findViewById(R.id.backToMainBtn)

        // Nastavenie onClickListeneru na tlačidlo pre návrat
        backToMain.setOnClickListener {
            // Ukončenie aktuálnej aktivity a návrat na hlavnú obrazovku
            finish()
        }

    }
}
