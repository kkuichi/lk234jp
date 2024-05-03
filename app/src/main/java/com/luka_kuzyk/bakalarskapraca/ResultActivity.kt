package com.luka_kuzyk.bakalarskapraca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.card.MaterialCardView

// Aktivita zodpovedná za zobrazenie výsledkov testu
class ResultActivity : AppCompatActivity() {
    private lateinit var correctResult: TextView
    private lateinit var wrongResult: TextView
    private lateinit var resultInfo: TextView
    private lateinit var resultScore: TextView
    private lateinit var resultImage: ImageView
    private lateinit var goHome: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // Inicializácia prvkov rozhrania
        goHome = findViewById(R.id.returnHome)
        correctResult = findViewById(R.id.correctScore)
        wrongResult = findViewById(R.id.wrongScore)
        resultInfo = findViewById(R.id.resultInfo)
        resultScore = findViewById(R.id.resultScore)
        resultImage = findViewById(R.id.resultImage)

        // Získanie výsledkov testu z intentu
        val correct = intent.getIntExtra("correct", 0)
        val wrong = intent.getIntExtra("wrong", 0)
        val testID = intent.getIntExtra("testID", 0)
        val total = correct + wrong
        val score = (correct * 100) / total

        // Aktualizácia pokroku používateľa
        User.testy[testID - 1] = score
        User.uploadUserProgressToFireStore()

        // Nastavenie textových polí na zobrazenie výsledkov
        correctResult.text = correct.toString()
        wrongResult.text = wrong.toString()
        resultScore.text = "$score/100"

        // Nastavenie informačného textu na základe skóre
        when {
            score < 51 -> resultInfo.text = getString(R.string.Advice6)
            score < 61 -> resultInfo.text = getString(R.string.Advice5)
            score < 71 -> resultInfo.text = getString(R.string.Advice4)
            score < 81 -> resultInfo.text = getString(R.string.Advice3)
            score < 91 -> resultInfo.text = getString(R.string.Advice2)
            score < 100 -> resultInfo.text = getString(R.string.Advice1)
        }

        // Nastavenie akcie pre tlačidlo pre návrat na hlavnú obrazovku
        goHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}