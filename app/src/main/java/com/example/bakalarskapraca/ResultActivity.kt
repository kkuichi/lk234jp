package com.example.bakalarskapraca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.card.MaterialCardView

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

        goHome = findViewById(R.id.returnHome);
        correctResult = findViewById(R.id.correctScore);
        wrongResult = findViewById(R.id.wrongScore);
        resultInfo = findViewById(R.id.resultInfo);
        resultScore = findViewById(R.id.resultScore);
        resultImage = findViewById(R.id.resultImage);

        val correct = intent.getIntExtra("correct", 0)
        val wrong = intent.getIntExtra("wrong", 0)
        val score = correct * 5

        correctResult.text = correct.toString();
        wrongResult.text = wrong.toString();
        resultScore.text = score.toString();

        val total = correct+wrong

        if (correct/total < 0.5) {
            resultInfo.setText("You have to take the test again");
//            resultImage.setImageResource(R.drawable.ic_sad);
        } else if (correct/total < 0.65) {
            resultInfo.setText("You have to try a little more");
//            resultImage.setImageResource(R.drawable.ic_neutral);
        } else if (correct/total < 0.85) {
            resultInfo.setText("You are pretty good");
//            resultImage.setImageResource(R.drawable.ic_smile);
        } else {
            resultInfo.setText("You are very good congratulations");
//            resultImage.setImageResource(R.drawable.ic_smile);
        }

        goHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }
}